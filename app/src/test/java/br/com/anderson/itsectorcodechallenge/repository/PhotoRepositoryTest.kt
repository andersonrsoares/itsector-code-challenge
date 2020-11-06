package br.com.anderson.itsectorcodechallenge.repository

import br.com.anderson.itsectorcodechallenge.ApiUtil
import br.com.anderson.itsectorcodechallenge.MockJSONDataSource
import br.com.anderson.itsectorcodechallenge.MockJSONDataSourceRule
import br.com.anderson.itsectorcodechallenge.dto.PhotoDTO
import br.com.anderson.itsectorcodechallenge.mapper.FotoMapper
import br.com.anderson.itsectorcodechallenge.mock
import br.com.anderson.itsectorcodechallenge.model.DataSourceResult
import br.com.anderson.itsectorcodechallenge.model.ErrorResult
import br.com.anderson.itsectorcodechallenge.service.UnsplashService
import com.google.gson.Gson
import com.google.gson.stream.MalformedJsonException
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.BDDMockito.given
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.TimeUnit
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

@RunWith(JUnit4::class)
class PhotoRepositoryTest {
    class ListPhotoDTO : ArrayList<PhotoDTO>()

    private val service = mock<UnsplashService>()
    private lateinit var photoRepository: PhotoRepository

    @Rule
    @JvmField
    val mockJSONDataSourceRule = MockJSONDataSourceRule(Gson())

    @Before
    fun setup() {
        photoRepository = PhotoRepositoryImpl(service, FotoMapper())
    }

    @Test
    @MockJSONDataSource(fileName = "api-response/photos_response_success.json", clazz = ListPhotoDTO::class)
    fun `test get photos`() {

        val remoteData = mockJSONDataSourceRule.getValue<List<PhotoDTO>>()
        val page = 1

        given(service.photos(page)).willReturn(Single.just(remoteData))

        val testSubscriber = photoRepository.getFotos(page).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue(DataSourceResult.create(remoteData.map {  FotoMapper().map(it) } ))
    }

    @Test
    fun `test get photos error 401`() {
        val page = 1

        given(service.photos(page)).willReturn(
            Single.error(
                HttpException(
                    Response.error<Single<List<PhotoDTO>>>(401, ApiUtil.loadfile("error_unauthorized.json").toResponseBody())
                )
            )
        )

        val testSubscriber = photoRepository.getFotos(page).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue {
            it.error is ErrorResult.GenericError && (it.error as ErrorResult.GenericError).errorMessage == "OAuth error: The access token is invalid"
        }
    }


    @Test
    fun `test get photos error 500`() {
        val page = 1

        given(service.photos(page)).willReturn(
            Single.error(
                MalformedJsonException("")
            )
        )

        val testSubscriber = photoRepository.getFotos(page).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue {
            it.error is ErrorResult.GenericError && (it.error as ErrorResult.GenericError).errorMessage == ""
        }
    }

    @Test
    fun `test get photos error json 404`() {
        val page = 1

        given(service.photos(page)).willReturn(
            Single.error(
                HttpException(
                    Response.error<Single<List<PhotoDTO>>>(404, ApiUtil.loadfile("json_empty.json").toResponseBody())
                )
            )
        )

        val testSubscriber = photoRepository.getFotos(page).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue {
            it.error is ErrorResult.NotFound
        }
    }

    @Test
    fun `test get photos error network timeout`() {
        val page = 1

        given(service.photos(page)).willReturn(
            Single.error(
                TimeoutException()
            )
        )

        val testSubscriber = photoRepository.getFotos(page).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue {
            it.error is ErrorResult.NetworkError
        }
    }

    @Test
    fun `test get photos error network unknown host`() {
        val page = 1

        given(service.photos(page)).willReturn(
            Single.error(
                UnknownHostException()
            )
        )

        val testSubscriber = photoRepository.getFotos(page).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue {
            it.error is ErrorResult.NetworkError
        }
    }

    @Test
    fun `test get photos error unknown`() {
        val page = 1

        given(service.photos(page)).willReturn(
            Single.error(
                Throwable("error")
            )
        )

        val testSubscriber = photoRepository.getFotos(page).test()

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)

        testSubscriber.assertNoErrors()
        testSubscriber.assertSubscribed()
        testSubscriber.assertComplete()
        testSubscriber.assertValue {
            (it.error as ErrorResult.GenericError).errorMessage == "error"
        }
    }

}

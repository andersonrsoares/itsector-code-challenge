package br.com.anderson.itsectorcodechallenge.listphoto

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.anderson.itsectorcodechallenge.*
import br.com.anderson.itsectorcodechallenge.dto.PhotoDTO
import br.com.anderson.itsectorcodechallenge.extras.extractMessage
import br.com.anderson.itsectorcodechallenge.model.DataSourceResult
import br.com.anderson.itsectorcodechallenge.model.Photo
import br.com.anderson.itsectorcodechallenge.provider.ResourceProvider
import br.com.anderson.itsectorcodechallenge.repository.PhotoRepository
import br.com.anderson.itsectorcodechallenge.ui.listphoto.ListPhotoViewModel
import com.google.gson.Gson
import com.google.gson.stream.MalformedJsonException
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.mockito.BDDMockito.*
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


@RunWith(JUnit4::class)
class ListPhotoViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val photoRepository = mock<PhotoRepository>()
    private val resourceProvider = mock<ResourceProvider>()

    private lateinit var listPhotoViewModel: ListPhotoViewModel


    @Before
    fun init() {
        listPhotoViewModel =
                ListPhotoViewModel(
                        photoRepository
            )
        listPhotoViewModel.resourceProvider = resourceProvider
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `list photos success`() {

        val remoteData = listOf(Photo(id = "1", smallUrl = "http://smallimage1.com",downloadUrl = "http://largeimage1.com" ),
                Photo(id = "2", smallUrl = "http://smallimage2.com",downloadUrl = "http://largeimage2.com" ) )

        given(photoRepository.getFotos(1)).willReturn(Observable.just(DataSourceResult.create(remoteData)))

        val observerData = mock<Observer<List<Photo>>>()
        val observerLoading = mock<Observer<Boolean>>()

        listPhotoViewModel.loading.observeForever(observerLoading)
        listPhotoViewModel.dataPhoto.observeForever(observerData)
        listPhotoViewModel.listPhotos()
        then(observerLoading).should().onChanged(true)
        then(photoRepository).should().getFotos(1)
        then(observerData).should().onChanged(remoteData)
        then(observerLoading).should(times(2)).onChanged(false)
    }


    @Test
    fun `list photos success data empty`() {

        val remoteData = listOf<Photo>()

        given(photoRepository.getFotos(1)).willReturn(Observable.just(DataSourceResult.create(remoteData)))

        val observerData = mock<Observer<List<Photo>>>()
        val observerLoading = mock<Observer<Boolean>>()

        listPhotoViewModel.loading.observeForever(observerLoading)
        listPhotoViewModel.dataPhoto.observeForever(observerData)
        listPhotoViewModel.listPhotos()
        then(observerLoading).should().onChanged(true)
        then(photoRepository).should().getFotos(1)
        then(observerData).should(never()).onChanged(null)
        then(observerLoading).should(times(2)).onChanged(false)
    }

    @Test
    fun `list error network timeout and retry`() {

        given(photoRepository.getFotos(1)).willReturn(Observable.just(DataSourceResult.error(TimeoutException())))
        given(resourceProvider.getString(ArgumentMatchers.anyInt())).willReturn("error network")

        val observerData = mock<Observer<List<Photo>>>()
        val observerLoading = mock<Observer<Boolean>>()
        val observeRetry = mock<Observer<String>>()

        listPhotoViewModel.loading.observeForever(observerLoading)
        listPhotoViewModel.dataPhoto.observeForever(observerData)
        listPhotoViewModel.retry.observeForever(observeRetry)
        listPhotoViewModel.listPhotos()
        then(observerLoading).should().onChanged(true)
        then(photoRepository).should().getFotos(1)
        then(observerData).should(never()).onChanged(null)
        then(observeRetry).should().onChanged("error network")

        given(photoRepository.getFotos(1)).willReturn(Observable.just(DataSourceResult.create(arrayListOf())))
        listPhotoViewModel.retry()
        then(observerLoading).should(times(5)).onChanged(false)
    }

    @Test
    fun `list error server 404`() {
        given(resourceProvider.getString(ArgumentMatchers.anyInt())).willReturn("error network")
        given(photoRepository.getFotos(1)).willReturn(Observable.just(DataSourceResult.error(
            HttpException(
                    Response.error<List<PhotoDTO>>(404,  "".toResponseBody())
            )
        )))

        val observerData = mock<Observer<List<Photo>>>()
        val observerLoading = mock<Observer<Boolean>>()
        val observeMessage = mock<Observer<String>>()

        listPhotoViewModel.loading.observeForever(observerLoading)
        listPhotoViewModel.dataPhoto.observeForever(observerData)
        listPhotoViewModel.message.observeForever(observeMessage)
        listPhotoViewModel.listPhotos()
        then(observerLoading).should().onChanged(true)
        then(photoRepository).should().getFotos(1)
        then(observerData).should(never()).onChanged(null)
        then(observeMessage).should().onChanged("error network")
        then(observerLoading).should(times(3)).onChanged(false)
    }

    @Test
    fun `list error server 500`() {
        given(photoRepository.getFotos(1)).willReturn(Observable.just(DataSourceResult.error(
                HttpException(
                        Response.error<List<PhotoDTO>>(500,  "error".toResponseBody())
                )
        )))

        val observerData = mock<Observer<List<Photo>>>()
        val observerLoading = mock<Observer<Boolean>>()
        val observeMessage = mock<Observer<String>>()

        listPhotoViewModel.loading.observeForever(observerLoading)
        listPhotoViewModel.dataPhoto.observeForever(observerData)
        listPhotoViewModel.message.observeForever(observeMessage)
        listPhotoViewModel.listPhotos()
        then(observerLoading).should().onChanged(true)
        then(photoRepository).should().getFotos(1)
        then(observerData).should(never()).onChanged(null)
        then(observeMessage).should().onChanged("error")
        then(observerLoading).should(times(3)).onChanged(false)
    }

    @Test
    fun `list error json parse`() {
        given(resourceProvider.getString(ArgumentMatchers.anyInt())).willReturn("error server")
        given(photoRepository.getFotos(1)).willReturn(Observable.just(DataSourceResult.error(
                MalformedJsonException("")
        )))

        val observerData = mock<Observer<List<Photo>>>()
        val observerLoading = mock<Observer<Boolean>>()
        val observeMessage = mock<Observer<String>>()

        listPhotoViewModel.loading.observeForever(observerLoading)
        listPhotoViewModel.dataPhoto.observeForever(observerData)
        listPhotoViewModel.message.observeForever(observeMessage)
        listPhotoViewModel.listPhotos()
        then(observerLoading).should().onChanged(true)
        then(photoRepository).should().getFotos(1)
        then(observerData).should(never()).onChanged(null)
        then(observeMessage).should().onChanged("error server")
        then(observerLoading).should(times(3)).onChanged(false)
    }

    @Test
    fun `list error generic`() {
        given(photoRepository.getFotos(1)).willReturn(Observable.just(DataSourceResult.error(
                Throwable("error")
        )))

        val observerData = mock<Observer<List<Photo>>>()
        val observerLoading = mock<Observer<Boolean>>()
        val observeMessage = mock<Observer<String>>()

        listPhotoViewModel.loading.observeForever(observerLoading)
        listPhotoViewModel.dataPhoto.observeForever(observerData)
        listPhotoViewModel.message.observeForever(observeMessage)
        listPhotoViewModel.listPhotos()
        then(observerLoading).should().onChanged(true)
        then(photoRepository).should().getFotos(1)
        then(observerData).should(never()).onChanged(null)
        then(observeMessage).should().onChanged("error")
        then(observerLoading).should(times(3)).onChanged(false)
    }

    @Test
    fun `list photos success page 2`() {
        val remoteData = listOf(Photo(id = "1", smallUrl = "http://smallimage1.com",downloadUrl = "http://largeimage1.com" ),
                Photo(id = "2", smallUrl = "http://smallimage2.com",downloadUrl = "http://largeimage2.com" ) )

        val remoteData2 = listOf(Photo(id = "1", smallUrl = "http://smallimage1.com",downloadUrl = "http://largeimage1.com" ),
                Photo(id = "2", smallUrl = "http://smallimage2.com",downloadUrl = "http://largeimage2.com" ) )


        given(photoRepository.getFotos(1)).willReturn(Observable.just(DataSourceResult.create(remoteData)))
        given(photoRepository.getFotos(2)).willReturn(Observable.just(DataSourceResult.create(remoteData2)))

        val observerData = mock<Observer<List<Photo>>>()
        val observerLoading = mock<Observer<Boolean>>()

        listPhotoViewModel.loading.observeForever(observerLoading)
        listPhotoViewModel.dataPhoto.observeForever(observerData)
        listPhotoViewModel.listPhotos()
        then(observerLoading).should().onChanged(true)
        then(photoRepository).should().getFotos(1)
        then(observerData).should().onChanged(remoteData)
        then(observerLoading).should(times(2)).onChanged(false)

        //page 2
        listPhotoViewModel.listScrolled(2, 3, 5)
        then(observerLoading).should(times(2)).onChanged(true)
        then(photoRepository).should().getFotos(2)
        then(observerData).should().onChanged(remoteData2)
        then(observerLoading).should(times(4)).onChanged(false)
    }
}

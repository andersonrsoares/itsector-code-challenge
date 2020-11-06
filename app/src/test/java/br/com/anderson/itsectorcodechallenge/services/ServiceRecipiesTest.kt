package br.com.anderson.itsectorcodechallenge.services

import br.com.anderson.itsectorcodechallenge.ApiUtil
import com.google.common.truth.Truth.assertThat
import com.google.gson.stream.MalformedJsonException
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException

@RunWith(JUnit4::class)
class ServiceRecipiesTest : BaseServiceTest() {

    @Test
    fun `test photos response success full data`() {
        // GIVEN
        ApiUtil.enqueueResponse(mockWebServer, "photos_response_success.json")

        val page  = 1
        val response = service.photos(page).test()

        // when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/photos?page=1&client_id=52ed5e63ad1915fed2bbfd2326aade6b8549b050fc8367a7c105567476df2a81")
        // THEN

        response.assertNoErrors()
        val photosResult = response.values().first()
        assertThat(photosResult.size).isEqualTo(10)
        assertThat(photosResult[0].id).isEqualTo("CMOP2QZ1bWg")
        assertThat(photosResult[0].links?.download).isEqualTo("https://unsplash.com/photos/CMOP2QZ1bWg/download")
        assertThat(photosResult[0].urls?.thumb).isEqualTo("https://images.unsplash.com/photo-1601758064224-c3c5ec910095?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjExMDY5OX0")
    }

    @Test
    fun `test photos response success null value`() {
        // GIVEN
        ApiUtil.enqueueResponse(mockWebServer, "photos_response_success_null_values.json")

        val page  = 1
        val response = service.photos(page).test()

        // when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/photos?page=1&client_id=52ed5e63ad1915fed2bbfd2326aade6b8549b050fc8367a7c105567476df2a81")
        // THEN

        response.assertNoErrors()
        val photosResult = response.values().first()
        assertThat(photosResult.size).isEqualTo(1)
        assertThat(photosResult[0].id).isEqualTo(null)
        assertThat(photosResult[0].links?.download).isEqualTo(null)
        assertThat(photosResult[0].urls?.thumb).isEqualTo(null)
    }



    @Test
    fun `test response not json`() {
        // GIVEN
        ApiUtil.enqueueResponse(mockWebServer, "error_json_response.html")

        val response = service.photos(1).test()
        // when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/photos?page=1&client_id=52ed5e63ad1915fed2bbfd2326aade6b8549b050fc8367a7c105567476df2a81")
        // THEN

        response.assertError {
            it is MalformedJsonException
        }
    }

    @Test
    fun `test unauthorized`() {
        // GIVEN
        ApiUtil.enqueueResponse(mockWebServer = mockWebServer, fileName = "error_unauthorized.json", statuscode = 401)

        val response = service.photos(1).test()
        // when
        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/photos?page=1&client_id=52ed5e63ad1915fed2bbfd2326aade6b8549b050fc8367a7c105567476df2a81")

        // THEN
        response.assertError {
            it is HttpException && (it as? HttpException)?.code() == 401
        }
    }
}

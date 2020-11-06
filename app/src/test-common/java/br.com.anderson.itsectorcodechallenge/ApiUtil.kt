package br.com.anderson.itsectorcodechallenge

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source

object ApiUtil {

    fun enqueueResponse(mockWebServer: MockWebServer, fileName: String, headers: Map<String, String> = emptyMap(), statuscode: Int = 200) {
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(loadfile(fileName))
                .setResponseCode(statuscode)
        )
    }

    fun loadfile(fileName: String): String = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")?.source()?.buffer()?.readString(Charsets.UTF_8) ?: ""
}

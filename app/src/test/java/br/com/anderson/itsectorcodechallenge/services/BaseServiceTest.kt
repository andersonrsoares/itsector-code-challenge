package br.com.anderson.itsectorcodechallenge.services

import br.com.anderson.itsectorcodechallenge.service.AuthorizationInterceptor
import br.com.anderson.itsectorcodechallenge.service.UnsplashService
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

open class BaseServiceTest {

    protected lateinit var service: UnsplashService

    protected lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
             .client(OkHttpClient().newBuilder().addInterceptor(AuthorizationInterceptor()).build())
            .build()
            .create(UnsplashService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }
}

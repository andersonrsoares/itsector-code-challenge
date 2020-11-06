package br.com.anderson.itsectorcodechallenge.service

import br.com.anderson.itsectorcodechallenge.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url: HttpUrl = request.url.newBuilder().addQueryParameter("client_id", BuildConfig.CLIENT_ID).build()
        return chain.proceed(request.newBuilder().url(url).build())
    }
}

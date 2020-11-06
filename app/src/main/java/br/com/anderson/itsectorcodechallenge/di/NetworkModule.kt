package br.com.anderson.itsectorcodechallenge.di

import android.app.Application
import br.com.anderson.itsectorcodechallenge.BuildConfig
import br.com.anderson.itsectorcodechallenge.service.AuthorizationInterceptor
import br.com.anderson.itsectorcodechallenge.service.UnsplashService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideService(okHttpClient: OkHttpClient): UnsplashService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(UnsplashService::class.java)
    }

    @Singleton
    @Provides
    fun okHttpClientCacheProvider(app: Application): Cache {
        return Cache(app.cacheDir, (5 * 1024 * 1024).toLong())
    }

    @Singleton
    @Provides
    fun okHttpClientProvider(autorizationInterceptor: AuthorizationInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(6000, TimeUnit.MILLISECONDS)
            .readTimeout((1000 * 60).toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout((1000 * 60).toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor(autorizationInterceptor)
            .apply {
                if (BuildConfig.DEBUG) {
                    val logInterceptor = HttpLoggingInterceptor()
                    logInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    this.addInterceptor(logInterceptor)
                }
            }.build()
    }

    @Singleton
    @Provides
    fun autorizationInterceptorProvider(): AuthorizationInterceptor {
        return AuthorizationInterceptor()
    }
}

package br.com.anderson.itsectorcodechallenge.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class AppModule {

   /* @Singleton
    @Provides
    fun provideResource(app: Application): ResourcesProvider {
        return ResourceProvider(app)
    }*/

}

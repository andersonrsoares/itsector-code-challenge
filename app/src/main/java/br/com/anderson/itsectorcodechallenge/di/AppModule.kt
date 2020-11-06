package br.com.anderson.itsectorcodechallenge.di

import dagger.Module

@Module(includes = [NetworkModule::class])
class AppModule {

   /* @Singleton
    @Provides
    fun provideResource(app: Application): ResourcesProvider {
        return ResourceProvider(app)
    }*/
}

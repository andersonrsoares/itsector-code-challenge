package br.com.anderson.itsectorcodechallenge.di

import android.app.Application
import br.com.anderson.itsectorcodechallenge.dto.PhotoDTO
import br.com.anderson.itsectorcodechallenge.mapper.FotoMapper
import br.com.anderson.itsectorcodechallenge.mapper.Mapper
import br.com.anderson.itsectorcodechallenge.model.Photo
import br.com.anderson.itsectorcodechallenge.provider.ResourceProvider
import br.com.anderson.itsectorcodechallenge.repository.PhotoRepository
import br.com.anderson.itsectorcodechallenge.repository.PhotoRepositoryImpl
import br.com.anderson.itsectorcodechallenge.service.UnsplashService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class AppModule {

    @Singleton
    @Provides
    fun providePhotoRepository(
        remoteDataSource: UnsplashService
    ): PhotoRepository {
        return PhotoRepositoryImpl(
            remoteDataSource,
            FotoMapper()
        )
    }

    @Singleton
    @Provides
    fun provideResource(app: Application): ResourceProvider {
        return ResourceProvider(app)
    }
}

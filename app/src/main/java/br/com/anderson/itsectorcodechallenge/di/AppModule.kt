package br.com.anderson.itsectorcodechallenge.di

import br.com.anderson.itsectorcodechallenge.dto.PhotoDTO
import br.com.anderson.itsectorcodechallenge.mapper.FotoMapper
import br.com.anderson.itsectorcodechallenge.mapper.Mapper
import br.com.anderson.itsectorcodechallenge.model.Photo
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
    fun provideFotoRepository(
            remoteDataSource: UnsplashService,
            fotoMapper: Mapper<PhotoDTO, Photo>
    ): PhotoRepository {
        return PhotoRepositoryImpl(
                remoteDataSource,
                fotoMapper
        )
    }

    @Singleton
    @Provides
    fun provideFotoMapper() : Mapper<PhotoDTO, Photo> = FotoMapper()

}

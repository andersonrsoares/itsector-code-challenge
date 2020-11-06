package br.com.anderson.itsectorcodechallenge.di

import br.com.anderson.itsectorcodechallenge.dto.PhotoDTO
import br.com.anderson.itsectorcodechallenge.mapper.FotoMapper
import br.com.anderson.itsectorcodechallenge.mapper.Mapper
import br.com.anderson.itsectorcodechallenge.model.Photo
import br.com.anderson.itsectorcodechallenge.repository.FotoRepository
import br.com.anderson.itsectorcodechallenge.repository.FotoRepositoryImpl
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
    ): FotoRepository {
        return FotoRepositoryImpl(
                remoteDataSource,
                fotoMapper
        )
    }

    @Singleton
    @Provides
    fun provideFotoMapper() : Mapper<PhotoDTO, Photo> = FotoMapper()

}

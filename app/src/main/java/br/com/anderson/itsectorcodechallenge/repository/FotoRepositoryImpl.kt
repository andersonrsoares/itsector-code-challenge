package br.com.anderson.itsectorcodechallenge.repository

import br.com.anderson.itsectorcodechallenge.dto.PhotoDTO
import br.com.anderson.itsectorcodechallenge.extras.transformToDataSourceResult
import br.com.anderson.itsectorcodechallenge.mapper.Mapper
import br.com.anderson.itsectorcodechallenge.model.DataSourceResult
import br.com.anderson.itsectorcodechallenge.model.Photo
import br.com.anderson.itsectorcodechallenge.service.UnsplashService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FotoRepositoryImpl @Inject constructor(
    val remoteDataSource: UnsplashService,
    val fotoMapper: Mapper<PhotoDTO,Photo>
) : FotoRepository{

    override fun getFotos(page: Int): Observable<DataSourceResult<List<Photo>>> {
        return remoteDataSource.photos(page)
                .subscribeOn(Schedulers.io())
                .map {
                    it.map { item-> fotoMapper.map(item) }
                }.transformToDataSourceResult().toObservable()
    }
}

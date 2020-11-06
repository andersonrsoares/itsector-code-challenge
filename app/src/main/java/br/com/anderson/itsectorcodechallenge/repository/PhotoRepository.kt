package br.com.anderson.itsectorcodechallenge.repository

import br.com.anderson.itsectorcodechallenge.model.DataSourceResult
import br.com.anderson.itsectorcodechallenge.model.Photo
import io.reactivex.Observable
import javax.inject.Singleton

@Singleton
interface PhotoRepository{

    fun getFotos(page: Int): Observable<DataSourceResult<List<Photo>>>

}

package br.com.anderson.itsectorcodechallenge.repository

import br.com.anderson.itsectorcodechallenge.mapper.FotoMapper
import br.com.anderson.itsectorcodechallenge.model.DataSourceResult
import br.com.anderson.itsectorcodechallenge.model.Foto
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Singleton

@Singleton
interface FotoRepository{

    fun getFotos(page: Int): Observable<DataSourceResult<List<Foto>>>

}

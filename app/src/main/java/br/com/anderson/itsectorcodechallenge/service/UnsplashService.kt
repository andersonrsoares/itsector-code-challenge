package br.com.anderson.itsectorcodechallenge.service

import br.com.anderson.itsectorcodechallenge.dto.PhotoDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * REST API access points
 */

interface UnsplashService {

    @GET("/photos")
    fun photos(@Query("page") page: Int): Single<List<PhotoDTO>>
}

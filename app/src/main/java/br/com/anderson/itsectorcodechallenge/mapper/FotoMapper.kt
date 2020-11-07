package br.com.anderson.itsectorcodechallenge.mapper

import br.com.anderson.itsectorcodechallenge.dto.PhotoDTO
import br.com.anderson.itsectorcodechallenge.model.Photo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FotoMapper @Inject constructor() : Mapper<PhotoDTO, Photo>() {

    override fun map(from: PhotoDTO): Photo = Photo(
        id = from.id.orEmpty(), downloadUrl = from.links?.download.orEmpty(), smallUrl = from.urls?.thumb.orEmpty()
    )
}

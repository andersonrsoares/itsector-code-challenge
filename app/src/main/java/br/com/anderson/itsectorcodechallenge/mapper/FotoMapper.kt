package br.com.anderson.itsectorcodechallenge.mapper


import br.com.anderson.itsectorcodechallenge.dto.FotoDTO
import br.com.anderson.itsectorcodechallenge.model.Foto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FotoMapper @Inject constructor() : Mapper<FotoDTO, Foto>() {

    override fun map(from: FotoDTO): Foto = Foto(
        id = from.id.orEmpty(), description = from.description.orEmpty(), downloadUrl = from.links?.download.orEmpty(), smallUrl = from.urls?.small.orEmpty()
    )
}

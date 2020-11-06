package br.com.anderson.itsectorcodechallenge.dto
import com.google.gson.annotations.SerializedName



data class PhotoDTO(
    val id: String? = null,
    @SerializedName("urls")
    val urls: UrlDTO? = null,
    @SerializedName("links")
    val links: LinkDTO? = null,
)

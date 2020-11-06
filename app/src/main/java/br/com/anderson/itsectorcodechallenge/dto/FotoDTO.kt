package br.com.anderson.itsectorcodechallenge.dto
import com.google.gson.annotations.SerializedName



data class FotoDTO(
    @SerializedName("blur_hash")
    val blurHash: String? = null,
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("height")
    val height: Int? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("urls")
    val urls: UrlDTO? = null,
    @SerializedName("width")
    val width: Int? = null,
    @SerializedName("urls")
    val links: LinkDTO? = null,
)

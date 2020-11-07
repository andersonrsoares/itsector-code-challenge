package br.com.anderson.itsectorcodechallenge.dto
import com.google.gson.annotations.SerializedName

open class ErrorDTO(
    @SerializedName("errors")
    var errors: List<String>? = null
)

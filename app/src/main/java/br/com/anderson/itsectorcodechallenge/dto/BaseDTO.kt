package br.com.anderson.itsectorcodechallenge.dto
import com.google.gson.annotations.SerializedName


open class BaseDTO(
    @SerializedName("errors")
    var errors: List<String>? = null
)
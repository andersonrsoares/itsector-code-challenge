package br.com.anderson.itsectorcodechallenge.dto

import com.google.gson.annotations.SerializedName

data class UrlDTO(
        @SerializedName("small")
        val small: String? = null,
        @SerializedName("thumb")
        val thumb: String? = null
)
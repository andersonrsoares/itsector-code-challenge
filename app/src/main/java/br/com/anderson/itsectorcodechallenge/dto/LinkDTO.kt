package br.com.anderson.itsectorcodechallenge.dto

import com.google.gson.annotations.SerializedName

data class LinkDTO(
    @SerializedName("download")
    val download: String? = null
)

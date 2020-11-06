package br.com.anderson.itsectorcodechallenge.model

import br.com.anderson.itsectorcodechallenge.extras.handleException


data class DataSourceResult<T>(val body: T? = null, val error: ErrorResult? = null) {
    companion object {
        fun <T> error(error: Throwable): DataSourceResult<T> {
            return DataSourceResult(error = error.handleException())
        }

        fun <T> create(body: T): DataSourceResult<T> {
            return DataSourceResult(body)
        }
    }
}

sealed class ErrorResult {
    object NotFound : ErrorResult()

    object ServerError : ErrorResult()

    object NetworkError : ErrorResult()

    data class GenericError(val errorMessage: String? = null) : ErrorResult()
}

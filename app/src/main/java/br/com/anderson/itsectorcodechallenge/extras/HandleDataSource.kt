package br.com.anderson.itsectorcodechallenge.extras


import br.com.anderson.itsectorcodechallenge.model.DataSourceResult
import br.com.anderson.itsectorcodechallenge.model.ErrorResult
import com.google.gson.Gson
import com.google.gson.stream.MalformedJsonException
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.HttpException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun <T> Single<T>.transformToDataSourceResult(): Single<DataSourceResult<T>> {
    return this.map {
        DataSourceResult.create(it)
    }.onErrorReturn {
        it.createDataSourceResult()
    }
}

fun <T> Maybe<T>.transformToDataSourceResult(): Maybe<DataSourceResult<T>> {
    return this.map { DataSourceResult.create(it) }.onErrorReturn { it.createDataSourceResult() }
}

fun <T> Throwable.createDataSourceResult(): DataSourceResult<T> {
    return DataSourceResult.error(this)
}

fun Throwable.handleException(): ErrorResult {
    return when (this) {
        is UnknownHostException, is TimeoutException -> ErrorResult.NetworkError
        is MalformedJsonException -> ErrorResult.ServerError
        is HttpException -> this.handleServerError()
        else -> ErrorResult.GenericError()
    }
}

fun HttpException.handleServerError(): ErrorResult {
    val message = extractMessage()
    return when (this.code()) {
        404 -> ErrorResult.NotFound
        else -> ErrorResult.GenericError(message)
    }
}

fun HttpException.extractMessage(): String {
    return this.response()?.errorBody()?.string().orEmpty()
}

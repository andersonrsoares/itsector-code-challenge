package br.com.anderson.itsectorcodechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.anderson.itsectorcodechallenge.provider.ResourceProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class BaseViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    protected val disposable = CompositeDisposable()
    protected var _message = MutableLiveData<String>()
    protected var _loading = MutableLiveData<Boolean>()
    protected var _clean = MutableLiveData<Boolean>()
    protected var _retry = MutableLiveData<String>()

    val message: LiveData<String>
        get() = _message

    val loading: LiveData<Boolean>
        get() = _loading

    val clean: LiveData<Boolean>
        get() = _clean

    val retry: LiveData<String>
        get() = _retry

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    protected fun complete() {
        _loading.postValue(false)
    }

//    protected fun error(error: Throwable) {
//        _message.postValue(resourceProvider.getString(R.string.message_error))
//        complete()
//        error.printStackTrace()
//    }
//
//    protected fun error(error: ErrorResult) {
//        when (error) {
//            is ErrorResult.GenericError -> _message.postValue(error.errorMessage)
//            is ErrorResult.ServerError -> _message.postValue(resourceProvider.getString(R.string.message_server_error))
//            is ErrorResult.NetworkError -> _retry.postValue(resourceProvider.getString(R.string.message_server_network_error_retry))
//            is ErrorResult.NotFound -> _message.postValue(resourceProvider.getString(R.string.message_not_found))
//        }
//        complete()
//    }

    open fun refresh() {
        _clean.postValue(true)
    }
}

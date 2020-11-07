package br.com.anderson.itsectorcodechallenge.ui.listphoto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.anderson.itsectorcodechallenge.model.DataSourceResult
import br.com.anderson.itsectorcodechallenge.model.Photo
import br.com.anderson.itsectorcodechallenge.repository.PhotoRepository
import br.com.anderson.itsectorcodechallenge.ui.viewmodel.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

const val VISIBLE_THRESHOLD = 2

class ListPhotoViewModel @Inject constructor(private val photoRepository: PhotoRepository) : BaseViewModel() {

    private var _dataPhoto = MutableLiveData<List<Photo>>()

    val dataPhoto: LiveData<List<Photo>>
        get() = _dataPhoto

    private var currentPage: Int = 1

    override fun refresh() {
        super.refresh()
        currentPage = 1
        listPhotos()
    }

    fun retry() {
        listPhotos()
    }

    fun listPhotos() {
        _loading.postValue(true)
        disposable.add(
            photoRepository
                .getFotos(currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::subscrible, this::error, this::complete)
        )
    }

    fun nextPagePhotos() {
        if (_loading.value == false) {
            currentPage++
            listPhotos()
        }
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val immutableQuery = dataPhoto.value
            if (immutableQuery != null) {
                nextPagePhotos()
            }
        }
    }

    private fun subscrible(result: DataSourceResult<List<Photo>>) {
        when {
            result.body != null -> emitList(result.body)
            result.error != null -> error(result.error)
        }
        complete()
    }

    private fun emitList(result: List<Photo>) {
        if (!result.isNullOrEmpty()) {
            _dataPhoto.postValue(appendToCurrent(result))
        }
    }

    private fun appendToCurrent(result: List<Photo>): List<Photo> {
        val list = _dataPhoto.value.orEmpty().toMutableList()
        if (currentPage == 1) {
            list.clear()
            _clean.postValue(true)
        }
        list.addAll(result)
        return list
    }
}

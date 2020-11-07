package br.com.anderson.itsectorcodechallenge.ui.listphoto

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.anderson.itsectorcodechallenge.R
import br.com.anderson.itsectorcodechallenge.databinding.FragmentListPhotoBinding
import br.com.anderson.itsectorcodechallenge.di.Injectable
import br.com.anderson.itsectorcodechallenge.extras.observe
import br.com.anderson.itsectorcodechallenge.model.Photo
import javax.inject.Inject

class ListPhotoFragment : Fragment(R.layout.fragment_list_photo), Injectable {

    lateinit var adapter: PhotoAdapter

    val viewModel: ListPhotoViewModel by viewModels {
        factory
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var fragmentBinding: FragmentListPhotoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        initrRefresh()
        initRetryButton()
        initScrollListener()
        initObservers()
        fetchPhotos()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBinding = FragmentListPhotoBinding.inflate(layoutInflater)
        viewModel.listPhotos()
        
    }

    private fun initRetryButton() {
        fragmentBinding?.retrybutton.setOnClickListener(this::onRetryClick)
    }

    fun onRetryClick(view: View) {
        viewModel.refresh()
        view.isVisible = false
    }

    private fun initrRefresh() {
        fragmentBinding.swiperefresh.setOnRefreshListener(this::onRefresh)
    }

    fun onRefresh() {
        viewModel.refresh()
    }

    private fun fetchPhotos() {
        viewModel.listPhotos()
    }

    private fun initObservers() {
        observe(viewModel.dataPhoto, this::onLoadDataCompletedChallenge)
        observe(viewModel.message, this::onMessage)
        observe(viewModel.loading, this::onLoading)
        observe(viewModel.clean, this::onClean)
        observe(viewModel.retry, this::onRetry)
    }

    private fun initRecycleView() {
        adapter = PhotoAdapter()
        fragmentBinding.recycleview.adapter = adapter
        fragmentBinding.recycleview.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        adapter.itemOnClick = this::onItemClick
    }

    private fun onItemClick(item: Photo) {

    }

    private fun initScrollListener() {
        val layoutManager = fragmentBinding.recycleview.layoutManager as LinearLayoutManager
        fragmentBinding.recycleview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }

    private fun onLoadDataCompletedChallenge(data: List<Photo>) {
        println("data - print onLoadDataCompletedChallenge")
        adapter.submitList(data)
    }

    private fun onMessage(data: String) {
        Toast.makeText(requireContext(), data, Toast.LENGTH_LONG).show()
    }

    private fun onLoading(data: Boolean) {
        fragmentBinding.progressloadingnextpage.isVisible = data && adapter.currentList.isNotEmpty()
        fragmentBinding.swiperefresh.isRefreshing = data && adapter.currentList.isEmpty()
    }

    private fun onClean(data: Boolean) {
        if (data)
            adapter.submitList(arrayListOf())
    }

    private fun onRetry(data: String) {
        Toast.makeText(requireContext(), data, Toast.LENGTH_LONG).show()
        fragmentBinding.retrybutton.isVisible = true
    }
}

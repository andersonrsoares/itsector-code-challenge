package br.com.anderson.itsectorcodechallenge.ui.listphoto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.anderson.itsectorcodechallenge.R
import br.com.anderson.itsectorcodechallenge.databinding.FragmentListPhotoBinding
import br.com.anderson.itsectorcodechallenge.di.Injectable
import br.com.anderson.itsectorcodechallenge.extras.autoCleared
import br.com.anderson.itsectorcodechallenge.extras.observe
import br.com.anderson.itsectorcodechallenge.model.Photo
import javax.inject.Inject

class ListPhotoFragment : Fragment(R.layout.fragment_list_photo), Injectable {

    private var adapter by autoCleared<PhotoAdapter>()
    private var fragmentBinding by autoCleared<FragmentListPhotoBinding>()

    val viewModel: ListPhotoViewModel by viewModels {
        factory
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        initRefresh()
        initRetryButton()
        initScrollListener()
        initObservers()
   }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentBinding = FragmentListPhotoBinding.inflate(layoutInflater, container, false)
        return fragmentBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchPhotos()
    }

    private fun initRetryButton() {
        fragmentBinding.retrybutton.setOnClickListener(this::onRetryClick)
    }

    fun onRetryClick(view: View) {
        viewModel.refresh()
        view.isVisible = false
    }

    private fun initRefresh() {
        fragmentBinding.swiperefresh.setOnRefreshListener(this::onRefresh)
    }

    fun onRefresh() {
        viewModel.refresh()
    }

    private fun fetchPhotos() {
        viewModel.listPhotos()
    }

    private fun initObservers() {
        observe(viewModel.dataPhoto, this::onLoadDataPhotos)
        observe(viewModel.message, this::onMessage)
        observe(viewModel.loading, this::onLoading)
        observe(viewModel.clean, this::onClean)
        observe(viewModel.retry, this::onRetry)
    }

    private fun initRecycleView() {
        adapter = PhotoAdapter()
        fragmentBinding.recycleview.adapter = adapter
        fragmentBinding.recycleview.layoutManager = GridLayoutManager(requireContext(),2).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        adapter.itemOnClick = this::onItemClick
    }

    private fun onItemClick(item: Photo) {
        navController().navigate(
            ListPhotoFragmentDirections.actionListFotoFragmentToFotoFragment(
                item.downloadUrl
            )
        )
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

    private fun onLoadDataPhotos(data: List<Photo>) {
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

    fun navController() = findNavController()
}

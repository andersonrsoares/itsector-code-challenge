package br.com.anderson.itsectorcodechallenge.ui.listphoto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.anderson.itsectorcodechallenge.R
import br.com.anderson.itsectorcodechallenge.model.Photo
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.concurrent.Executors

class PhotoAdapter : ListAdapter<Photo, PhotoAdapter.Holder>(
    AsyncDifferConfig.Builder<Photo>(diffCallback)
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
) {

    var itemOnClick: (Photo) -> Unit = { _ -> }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoAdapter.Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_photo, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        binds(holder, getItem(position))
    }

    private fun binds(holder: Holder, data: Photo) {
        with(holder) {
            itemView.setOnClickListener {
                itemOnClick(data)
            }

            Glide.with(image.context).load(data.smallUrl).apply(RequestOptions().placeholder(R.drawable.image_placeholder).centerCrop()).into(image)
        }
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
    }

    companion object {
        private val diffCallback: DiffUtil.ItemCallback<Photo> =
            object : DiffUtil.ItemCallback<Photo>() {
                override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                    return oldItem == newItem
                }
            }
    }
}

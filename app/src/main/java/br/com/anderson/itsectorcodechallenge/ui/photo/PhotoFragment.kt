package br.com.anderson.itsectorcodechallenge.ui.photo

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import br.com.anderson.itsectorcodechallenge.R
import br.com.anderson.itsectorcodechallenge.databinding.FragmentPhotoBinding
import br.com.anderson.itsectorcodechallenge.di.Injectable
import br.com.anderson.itsectorcodechallenge.extras.autoCleared
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class PhotoFragment : Fragment(R.layout.fragment_photo), Injectable {

    private var fragmentBinding by autoCleared<FragmentPhotoBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentBinding = FragmentPhotoBinding.inflate(layoutInflater, container, false)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun getUrl() = PhotoFragmentArgs.fromBundle(
        requireArguments()
    ).photoUrl

    fun init() {
        Glide.with(this)
            .load(getUrl())
            .dontAnimate()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    fragmentBinding.progress.isVisible = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    fragmentBinding.progress.isVisible = false
                    return false
                }
            })
            .into(fragmentBinding.fullimage)
    }
}

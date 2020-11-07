package br.com.anderson.itsectorcodechallenge.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.anderson.itsectorcodechallenge.R
import br.com.anderson.itsectorcodechallenge.databinding.FragmentListPhotoBinding
import br.com.anderson.itsectorcodechallenge.databinding.FragmentPhotoBinding
import br.com.anderson.itsectorcodechallenge.di.Injectable
import br.com.anderson.itsectorcodechallenge.extras.autoCleared
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PhotoFragment : Fragment(R.layout.fragment_photo), Injectable {

    private var fragmentBinding by autoCleared<FragmentPhotoBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentBinding = FragmentPhotoBinding.inflate(layoutInflater, container, false)
        return fragmentBinding.root
    }

    private fun getUrl() = PhotoFragmentArgs.fromBundle(
        requireArguments()
    ).photoUrl

    fun init(){
        Glide.with(requireContext()).load(getUrl()).apply(
            RequestOptions()
                .placeholder(R.drawable.image_placeholder)
                .centerCrop()
        ).into(fragmentBinding.fullimage)

    }
}

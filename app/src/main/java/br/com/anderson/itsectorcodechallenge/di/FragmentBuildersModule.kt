package br.com.anderson.itsectorcodechallenge.di

import br.com.anderson.itsectorcodechallenge.ui.listphoto.ListPhotoFragment
import br.com.anderson.itsectorcodechallenge.ui.photo.PhonoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeListFotoFragment(): ListPhotoFragment

    @ContributesAndroidInjector
    abstract fun contributeFotoFragment(): PhonoFragment
}

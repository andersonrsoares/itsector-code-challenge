package br.com.anderson.itsectorcodechallenge.di

import br.com.anderson.itsectorcodechallenge.ui.foto.FotoFragment
import br.com.anderson.itsectorcodechallenge.ui.listfoto.ListFotoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeListFotoFragment(): ListFotoFragment

    @ContributesAndroidInjector
    abstract fun contributeFotoFragment(): FotoFragment
}

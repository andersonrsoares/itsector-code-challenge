package br.com.anderson.itsectorcodechallenge.di

import androidx.lifecycle.ViewModelProvider
import br.com.anderson.itsectorcodechallenge.ui.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

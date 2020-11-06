package br.com.anderson.itsectorcodechallenge

import android.app.Application
import br.com.anderson.itsectorcodechallenge.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class ChallengeApp : Application(), HasAndroidInjector {

    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}

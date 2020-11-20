package edu.utap.mytrivia

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupTimber()
        installStetho()
    }

    private fun installStetho() {
        Stetho.initializeWithDefaults(this)
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }

}
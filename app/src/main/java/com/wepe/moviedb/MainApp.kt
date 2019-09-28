package com.wepe.moviedb

import android.app.Application
import com.wepe.moviedb.koin.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApp)
            modules(appComponent)
        }
    }
}
package com.example.moviedb

import android.app.Application
import com.example.moviedb.koin.appComponent
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
package com.example.weatherretrofit2

import android.app.Application
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
        super.onCreate()
    }
}
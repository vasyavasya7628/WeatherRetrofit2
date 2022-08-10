package com.example.weatherretrofit2

import android.app.Application
import com.example.weatherretrofit2.data.WeatherApi
import timber.log.Timber

class App : Application() {
    val api: WeatherApi = WeatherApi.create()
    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        super.onCreate()
    }
}
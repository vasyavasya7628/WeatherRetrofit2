package com.example.weatherretrofit2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment

class WeatherFragment : Fragment() {
    private var savedList: List<WeatherUI> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun setWeather(list: List<WeatherUI>) {
        savedList = list
    }
    fun getWeather() = savedList
}
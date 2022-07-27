package com.example.weatherretrofit2.viewmodels

import androidx.lifecycle.ViewModel
import com.example.weatherretrofit2.ui.WeatherUI

class WeatherViewModel : ViewModel() {

    var weathers: MutableList<WeatherUI> = mutableListOf()
    fun setWeather(weather: List<WeatherUI>) {
        weathers.addAll(weather)
    }
}
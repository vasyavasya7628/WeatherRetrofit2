package com.example.weatherretrofit2.model

import androidx.lifecycle.ViewModel
import com.example.weatherretrofit2.ui.WeatherUI

class WeatherViewModel : ViewModel() {

    var weathers: MutableList<WeatherUI> = mutableListOf()
        private set

    fun setWeather(weather: List<WeatherUI>) {
        weathers.addAll(weather)
    }
}
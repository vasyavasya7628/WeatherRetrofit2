package com.example.weatherretrofit2.model

import androidx.lifecycle.ViewModel
import com.example.weatherretrofit2.data.WeatherLocal

class WeatherViewModel : ViewModel() {

    var weathers: MutableList<WeatherLocal> = mutableListOf()
        private set

    fun setWeather(weather: List<WeatherLocal>) {
        weathers.addAll(weather)
    }
}
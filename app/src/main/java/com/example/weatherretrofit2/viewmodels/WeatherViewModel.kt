package com.example.weatherretrofit2.viewmodels

import androidx.lifecycle.ViewModel
import com.example.weatherretrofit2.ui.WeatherUI

class WeatherViewModel : ViewModel() {

    private var weathers: List<WeatherUI> = listOf()
    fun setWeather(weather: List<WeatherUI>) {
        weathers = weather
    }
    fun getWeather(): List<WeatherUI> {
        return weathers
    }
}
package com.example.weatherretrofit2.data

import com.example.weatherretrofit2.ui.WeatherUI

fun Description.toUI(): WeatherUI {
    return WeatherUI(
        dt = dt,
        temp = main.temp,
        pressure = main.pressure,
        icon = weather.first().icon
    )
}
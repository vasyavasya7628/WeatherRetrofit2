package com.example.weatherretrofit2.data

fun WeatherNW.Description.toUI(): WeatherUI {
    return WeatherUI(
        dt = dt,
        temp = main.temp,
        pressure = main.pressure,
        icon = weather.first().icon
    )
}
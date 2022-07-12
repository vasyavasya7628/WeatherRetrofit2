package com.example.weatherretrofit2.data

fun WeatherNW.Description.toDomain(): WeatherUI {
    return WeatherUI(
        dt = dt,
        temp = main.temp,
        pressure = main.pressure,
        icon = weather.first().icon
    )
}
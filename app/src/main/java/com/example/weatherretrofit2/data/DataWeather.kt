package com.example.weatherretrofit2.data

data class DataWeather(
    val dt: Int,
    val temp: Double,
    val pressure: Int,
    val icon: String
)

data class WeatherIconDomain(
    val icon: String
)
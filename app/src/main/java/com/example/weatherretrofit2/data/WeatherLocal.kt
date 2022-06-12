package com.example.weatherretrofit2.data

data class WeatherLocal(
    val dt: Int,
    val temp: Double,
    val pressure: Int,
    val icon: String
)

data class WeatherIconLocal(
    val icon: String
)
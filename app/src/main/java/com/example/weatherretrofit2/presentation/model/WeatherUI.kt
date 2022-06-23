package com.example.weatherretrofit2.presentation.model

data class WeatherUI(
    val dt: Int,
    val temp: Double,
    val pressure: Int,
    val icon: String
)
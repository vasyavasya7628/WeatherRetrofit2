package com.example.weatherretrofit2

import com.example.weatherretrofit2.data.DataWeather
import com.example.weatherretrofit2.data.WeatherDesrcNw
import com.example.weatherretrofit2.data.WeatherIconDomain
import com.example.weatherretrofit2.data.WeatherIconNet

fun WeatherDesrcNw.toDomain(): DataWeather{
    return DataWeather(
        dt = dt,
        temp = main.temp,
        pressure = main.pressure,
        icon = weather.toDomain().icon

    )
}

fun List<WeatherIconNet>.toDomain(): WeatherIconDomain{
    val last: WeatherIconNet = this.last()

    return WeatherIconDomain(
        icon = last.icon
    )
}
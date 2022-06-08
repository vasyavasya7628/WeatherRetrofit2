package com.example.weatherretrofit2.data

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
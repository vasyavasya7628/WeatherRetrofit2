package com.example.weatherretrofit2.data

fun WeatherNW.Description.toDomain(): WeatherLocal {
    return WeatherLocal(
        dt = dt,
        temp = main.temp,
        pressure = main.pressure,
        icon = weather.toDomain().icon
    )
}

fun List<WeatherNW.WeatherIconNet>.toDomain(): WeatherIconLocal {
    val last: WeatherNW.WeatherIconNet = this.last()

    return WeatherIconLocal(
        icon = last.icon
    )
}
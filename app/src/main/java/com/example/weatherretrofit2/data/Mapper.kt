package com.example.weatherretrofit2.data

fun WeatherDescriptionNw.toDomain(): WeatherLocal {
    return WeatherLocal(
        dt = dt,
        temp = main.temp,
        pressure = main.pressure,
        icon = weather.toDomain().icon

    )
}

fun List<WeatherIconNet>.toDomain(): WeatherIconLocal {
    val last: WeatherIconNet = this.last()

    return WeatherIconLocal(
        icon = last.icon
    )
}
package com.example.weatherretrofit2.data

fun WeatherDescriptionNw.toDomain(): DataWeather {//extension function

    return DataWeather(
        dt=dt,
        temp = main.temp,
        pressure = main.pressure,
        icon = this.  toDomain().icon
    )

}

fun List<WeatherNw>.toDomain(): Weather {
    val last: WeatherNw = this.last()

    return Weather(
        icon = last.
    )
}
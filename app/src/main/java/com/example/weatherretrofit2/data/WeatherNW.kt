package com.example.weatherretrofit2.data


import com.google.gson.annotations.SerializedName

data class WeatherNW(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Description>,
    val message: Int
) {
    data class City(
        @SerializedName("coord")
        val coord: Coord,
        @SerializedName("country")
        val country: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("population")
        val population: Int,
        @SerializedName("sunrise")
        val sunrise: Int,
        @SerializedName("sunset")
        val sunset: Int,
        @SerializedName("timezone")
        val timezone: Int
    )

    data class Coord(
        @SerializedName("lat")
        val lat: Double,
        @SerializedName("lon")
        val lon: Double
    )


    data class Description(
        @SerializedName("clouds")
        val clouds: Clouds,
        @SerializedName("dt")
        val dt: Int,
        @SerializedName("dt_txt")
        val dtTxt: String,
        @SerializedName("main")
        val main: Main,
        @SerializedName("pop")
        val pop: Double,
        @SerializedName("rain")
        val rain: Rain,
        @SerializedName("sys")
        val sys: Sys,
        @SerializedName("visibility")
        val visibility: Int,
        @SerializedName("weather")
        val weather: List<WeatherIconNet>,
        @SerializedName("wind")
        val wind: Wind
    )

    data class Clouds(
        @SerializedName("all")
        val all: Int
    )

    data class Main(
        @SerializedName("feels_like")
        val feelsLike: Double,
        @SerializedName("grnd_level")
        val grndLevel: Int,
        @SerializedName("humidity")
        val humidity: Int,
        @SerializedName("pressure")
        val pressure: Int,
        @SerializedName("sea_level")
        val seaLevel: Int,
        @SerializedName("temp")
        val temp: Double,
        @SerializedName("temp_kf")
        val tempKf: Double,
        @SerializedName("temp_max")
        val tempMax: Double,
        @SerializedName("temp_min")
        val tempMin: Double
    )

    data class Rain(
        @SerializedName("3h")
        val h: Double
    )

    data class Sys(
        @SerializedName("pod")
        val pod: String
    )

    data class WeatherIconNet(
        val description: String,
        val icon: String,
        val id: Int,
        val main: String
    )

    data class Wind(
        @SerializedName("deg")
        val deg: Int,
        @SerializedName("gust")
        val gust: Double,
        @SerializedName("speed")
        val speed: Double
    )
}
package com.example.weatherretrofit2

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
class WeatherApi {

    //https://api.openweathermap.org/data/2.5/forecast?id=1503901&appid=2ce0a504eccbb5cc5fdb54b14b60fab2
    interface WeatherApi {
        @GET("forecast")
        fun getWeather(
            @Query("id")
            id: String,
            @Query("appid")
            appid:String,
            @Query("units")
            measurement:String

        ): Call<DataWeatherFromNet>

        companion object {
            fun create(): WeatherApi {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
                return retrofit.create(WeatherApi::class.java)
            }
        }

    }
}
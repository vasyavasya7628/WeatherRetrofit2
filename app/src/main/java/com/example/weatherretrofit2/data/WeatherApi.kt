package com.example.weatherretrofit2.data

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

interface WeatherApi {
    @GET("forecast")
    fun getForecast(
        @Query("id")
        id: String,
        @Query("units")
        units: String,
        @Query("appid")
        appid: String
    ): Call<WeatherNW>

    companion object {
        fun create(): WeatherApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(WeatherApi::class.java)
        }
    }
}
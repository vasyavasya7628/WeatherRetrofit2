package com.example.weatherretrofit2.data

import com.example.weatherretrofit2.data.DataWeatherFromNet
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.openweathermap.org/data/3.0/"
    interface WeatherApi {
        @GET("forecast")
        fun getWeather(
            @Query("daily")
            day: String,
            @Query("lat")
            lat:String,
            @Query("lon")
            lon:String,
            @Query("units")
            units:String,
            @Query("appid")
            apikey:String

        ): Call<DataWeatherFromNet>

        companion object {
            fun create(): WeatherApi {
                val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                val httpClient: OkHttpClient.Builder = OkHttpClient().newBuilder()
                httpClient.addInterceptor(loggingInterceptor)

                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .build()
                return retrofit.create(WeatherApi::class.java)
            }
        }

    }
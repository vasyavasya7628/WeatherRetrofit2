package com.example.weatherretrofit2.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
interface WeatherApi {
    @GET("forecast")
    fun getForecast(
        @Query("id")
        id: String,
        @Query("units")
        units: String,
        @Query("appid")
        appid:String
    ): Call<DataWeatherFromNet>

    companion object {
        fun create(): WeatherApi {
            val netLog = run {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.apply {
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                }
            }
            val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(netLog)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(WeatherApi::class.java)
        }
    }
}




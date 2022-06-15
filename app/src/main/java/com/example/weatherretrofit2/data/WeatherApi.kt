package com.example.weatherretrofit2

import com.example.weatherretrofit2.data.DataWeatherFromNet
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
import java.util.concurrent.TimeUnit

//doc https://openweathermap.org/forecast5
//https://api.openweathermap.org/data/2.5/forecast?id=1503901&units=metric&appid=2ce0a504eccbb5cc5fdb54b14b60fab2 для Api
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
        fun create(): WeatherApi{
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




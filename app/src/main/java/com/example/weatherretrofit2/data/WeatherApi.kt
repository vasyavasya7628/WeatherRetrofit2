package com.example.weatherretrofit2

import com.example.weatherretrofit2.data.DataFromNet
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
import java.util.logging.Level

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
    ): Call<DataFromNet>

    companion object {
        fun create(): WeatherApi{
            val netLogging = HttpLoggingInterceptor()
            netLogging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient: OkHttpClient.Builder = OkHttpClient().newBuilder()
            httpClient.addInterceptor(netLogging)

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
            Timber.d(retrofit.toString())
            return retrofit.create(WeatherApi::class.java)
        }

    }


}




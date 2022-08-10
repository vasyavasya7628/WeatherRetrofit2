package com.example.weatherretrofit2.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherretrofit2.App
import com.example.weatherretrofit2.data.WeatherNW
import com.example.weatherretrofit2.data.toUI
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

private const val ID = "1503901"
private const val MEASUREMENT = "metric"
private const val API_KEY = "2ce0a504eccbb5cc5fdb54b14b60fab2"

class WeatherViewModel : ViewModel() {
    private var _weather: MutableLiveData<List<WeatherUI>> =
        MutableLiveData<List<WeatherUI>>()

    init {
        getDataFromNet()
    }

    fun getWeather() = _weather

    private fun getDataFromNet() {
        App().api.getForecast(
            ID,
            MEASUREMENT,
            API_KEY
        ).enqueue(object : retrofit2.Callback<WeatherNW> {
            override fun onResponse(
                call: Call<WeatherNW>,
                response: Response<WeatherNW>,
            ) {
                val weathers: List<WeatherUI> = response.body()?.list?.map { weatherNW ->
                    weatherNW.toUI()
                }.orEmpty()
                _weather.value = weathers
            }

            override fun onFailure(call: Call<WeatherNW>, t: Throwable) {
                Timber.e(t)
            }
        })
    }
}
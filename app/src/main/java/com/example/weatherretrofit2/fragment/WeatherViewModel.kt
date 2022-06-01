package com.example.weatherretrofit2.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherretrofit2.data.DataWeatherFromNet
import com.example.weatherretrofit2.data.WeatherApi
import com.example.weatherretrofit2.domain.DataWeather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query
import timber.log.Timber

//http://api.openweathermap.org/data/2.5/forecast?lat=55.3443&lon=86.0621&units=metric&appid=2ce0a504eccbb5cc5fdb54b14b60fab2
private const val FORECASTID = ""
private const val LAT = "55.3443"
private const val LON = "86.0621"
private const val DAY = "day"
private const val APIKEY = "2ce0a504eccbb5cc5fdb54b14b60fab2"
private const val WEATHERMEASUREMENT = "metric"

class WeatherViewModel : ViewModel() {
    private val api: WeatherApi = WeatherApi.create()
    private var _weathers: MutableLiveData<List<DataWeather>> = MutableLiveData<List<DataWeather>>()
    val weathers: LiveData<List<DataWeather>> get() = _weathers

    init {
        getDataFromNetwork()
    }

    private fun getDataFromNetwork() {
        api.getWeather(
            FORECASTID,
            LAT,
            LON,
            WEATHERMEASUREMENT,
            APIKEY
        )

            .enqueue(object : Callback<DataWeatherFromNet> {
                override fun onResponse(
                    call: Call<DataWeatherFromNet>,
                    response: Response<DataWeatherFromNet>
                ) {
                    if (response.isSuccessful) {
                        val weather: DataWeatherFromNet = response.body() as DataWeatherFromNet
                        val weatherDomain: List<DataWeather> = weather.list.map { weatherNw ->
                            weatherNw.toDomain()
                        }
                        _weathers.value = weatherDomain
                    } else Timber.d(response.message())
                }

                override fun onFailure(call: Call<DataWeatherFromNet>, t: Throwable) {
                    Timber.d("CRASHED")
                }
            })
    }


}
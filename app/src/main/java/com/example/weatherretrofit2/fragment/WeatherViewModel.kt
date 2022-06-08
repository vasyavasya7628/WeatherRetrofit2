package com.example.weatherretrofit2.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherretrofit2.WeatherApi
import com.example.weatherretrofit2.data.DataWeather
import com.example.weatherretrofit2.data.DataWeatherFromNet
import com.example.weatherretrofit2.data.toDomain
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

private const val ID = "1503901"
private const val MEASUREMENT = "metric"
private const val API_KEY = "2ce0a504eccbb5cc5fdb54b14b60fab2"

class WeatherViewModel: ViewModel() {
    private val api: WeatherApi = WeatherApi.create()
    private var _weather: MutableLiveData<List<DataWeather>> = MutableLiveData<List<DataWeather>>()
    val weatherExport: LiveData<List<DataWeather>> get() = _weather

    init {
        getDataFromNet()
    }

    //https://api.openweathermap.org/data/2.5/forecast?id=1503901&units=metric&appid=2ce0a504eccbb5cc5fdb54b14b60fab2
    private fun getDataFromNet() {
        api.getForecast(
            ID,
            MEASUREMENT,
            API_KEY
        ).enqueue(object : retrofit2.Callback<DataWeatherFromNet>{
            override fun onResponse(
                call: Call<DataWeatherFromNet>,
                response: Response<DataWeatherFromNet>
            ) {
                if (response.isSuccessful) {
                    val weather: DataWeatherFromNet = response.body() as DataWeatherFromNet
                    val weatherDomain: List<DataWeather> = weather.list.map { weatherNw ->
                        weatherNw.toDomain()
                    }
                    _weather.value = weatherDomain
                } else Timber.d("ОТВЕТ", response.message())
            }

            override fun onFailure(call: Call<DataWeatherFromNet>, t: Throwable) {
                Timber.d("Ошибка подлючения или запрос был составлен не правильно")
            }

        })

    }
}
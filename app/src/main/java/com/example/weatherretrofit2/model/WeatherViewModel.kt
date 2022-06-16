package com.example.weatherretrofit2.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherretrofit2.data.WeatherApi
import com.example.weatherretrofit2.data.WeatherLocal
import com.example.weatherretrofit2.data.WeatherNetwork
import com.example.weatherretrofit2.data.toDomain
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber


private const val ID = "1503901"
private const val MEASUREMENT = "metric"
private const val API_KEY = "2ce0a504eccbb5cc5fdb54b14b60fab2"

class WeatherViewModel : ViewModel() {
    private val api: WeatherApi = WeatherApi.create()
    private var _weather: MutableLiveData<List<WeatherLocal>> =
        MutableLiveData<List<WeatherLocal>>()
    val weatherExport: LiveData<List<WeatherLocal>> get() = _weather

    init {
        getDataFromNet()
    }

    private fun getDataFromNet() {
        api.getForecast(
            ID,
            MEASUREMENT,
            API_KEY
        ).enqueue(object : retrofit2.Callback<WeatherNetwork> {
            override fun onResponse(
                call: Call<WeatherNetwork>,
                response: Response<WeatherNetwork>
            ) {
                if (response.isSuccessful) {
                    val weather: WeatherNetwork = response.body() as WeatherNetwork
                    val weatherDomain: List<WeatherLocal> = weather.list.map { weatherNw ->
                        weatherNw.toDomain()
                    }
                    _weather.value = weatherDomain
                } else Timber.d("ОТВЕТ", response.message())
            }

            override fun onFailure(call: Call<WeatherNetwork>, t: Throwable) {
                Timber.d("Ошибка подлючения или запрос был составлен не правильно")
            }
        })
    }
}
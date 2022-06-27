package com.example.weatherretrofit2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherretrofit2.data.WeatherApi
import com.example.weatherretrofit2.data.WeatherNW
import com.example.weatherretrofit2.data.WeatherStore
import com.example.weatherretrofit2.data.toUI
import com.example.weatherretrofit2.databinding.ActivityMainBinding
import com.example.weatherretrofit2.ui.WeatherUI
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

private const val ID = "1503901"
private const val MEASUREMENT = "metric"
private const val API_KEY = "2ce0a504eccbb5cc5fdb54b14b60fab2"

class MainActivity : AppCompatActivity() {
    private val api: WeatherApi = WeatherApi.create()
    private val weatherAdapter = WeatherAdapter()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        loadWeather()
    }

    private fun loadWeather() {
        if (WeatherStore.tempWeather.isNullOrBlank()) {
            loadFromNetwork()
        } else {
            loadFromStore()
        }
    }

    private fun loadFromStore() {
        val weathers = Gson().fromJson<List<WeatherUI>>(
            WeatherStore.tempWeather,
            object : TypeToken<List<WeatherUI>>() {}.type
        ).toMutableList()
        weatherAdapter.submitList(weathers)
    }

    private fun loadFromNetwork() {
        api.getForecast(
            ID,
            MEASUREMENT,
            API_KEY
        ).enqueue(object : retrofit2.Callback<WeatherNW> {
            override fun onResponse(
                call: Call<WeatherNW>,
                response: Response<WeatherNW>
            ) {
                if (response.isSuccessful) {
                    val weathers: List<WeatherUI> = response.body()?.list?.map { weatherNw ->
                        weatherNw.toUI()
                    }.orEmpty()
                    WeatherStore.tempWeather = Gson().toJson(weathers)
                    weatherAdapter.submitList(weathers)
                    Timber.tag("NETWORK").d("СЕТЬ")
                } else Timber.d("ОТВЕТ", response.message())
            }

            override fun onFailure(call: Call<WeatherNW>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    private fun initRecyclerView() {
        binding.recyclerViewWeather.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewWeather.adapter = weatherAdapter
    }
}
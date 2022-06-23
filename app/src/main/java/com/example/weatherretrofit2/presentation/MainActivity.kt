package com.example.weatherretrofit2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherretrofit2.data.WeatherApi
import com.example.weatherretrofit2.data.WeatherNW
import com.example.weatherretrofit2.data.WeatherUI
import com.example.weatherretrofit2.data.toUI
import com.example.weatherretrofit2.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

private const val STATE_PLUS = 15 //сделал 15 градусов для проверки разных холдеров
private const val ID = "1503901"
private const val MEASUREMENT = "metric"
private const val API_KEY = "2ce0a504eccbb5cc5fdb54b14b60fab2"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherAdapter = WeatherAdapter()
    private val api: WeatherApi = WeatherApi.create()
    private var tempWeather: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            loadWeather()
        } else Timber.d("Произошел перевот экрана, данные восстановлены из переменной")

        initRecyclerView()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val gson = Gson()
        val weatherDomain = gson.fromJson<List<WeatherUI>>(
            savedInstanceState.getString("1", ""),
            object : TypeToken<List<WeatherUI>>() {}.type
        )
        submitDataToAdapter(weatherDomain)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("1", tempWeather)
    }


    private fun loadWeather() {
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
                    tempWeather = Gson().toJson(weathers)
                    submitDataToAdapter(weathers)
                } else Timber.d("ОТВЕТ", response.message())
            }

            override fun onFailure(call: Call<WeatherNW>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    private fun submitDataToAdapter(weatherUI: List<WeatherUI>) {
        val list = mutableListOf<DividerViewHolder>()
        weatherUI.map { weather ->
            if (weather.temp > STATE_PLUS) {
                list.add(DividerViewHolder.WeatherPlus(weather))
            } else {
                list.add(DividerViewHolder.WeatherMinus(weather))
            }
        }

        weatherAdapter.submitList(list.toMutableList())
    }

    private fun initRecyclerView() {
        binding.recyclerViewWeather.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewWeather.adapter = weatherAdapter
    }
}
package com.example.weatherretrofit2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherretrofit2.data.WeatherApi
import com.example.weatherretrofit2.data.WeatherNW
import com.example.weatherretrofit2.data.WeatherUI
import com.example.weatherretrofit2.data.toDomain
import com.example.weatherretrofit2.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

private const val ID = "1503901"
private const val MEASUREMENT = "metric"
private const val API_KEY = "2ce0a504eccbb5cc5fdb54b14b60fab2"
private const val KEY = "1"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherAdapter = WeatherAdapter()
    private val api: WeatherApi = WeatherApi.create()
    private val weatherFragment = WeatherFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        loadWeather()
    }

    private fun loadWeather() {
        if (weatherFragment.getWeather() == null) {
            loadFromNetwork()
        } else {
            loadFromFragment()
        }
    }

    private fun loadFromNetwork() {
        api.getForecast(
            ID,
            MEASUREMENT,
            API_KEY
        ).enqueue(object : retrofit2.Callback<WeatherNW> {
            override fun onResponse(
                call: Call<WeatherNW>,
                response: Response<WeatherNW>,
            ) {
                val weathers: List<WeatherUI> = response.body()?.list?.map { weatherNw ->
                    weatherNw.toDomain()
                }.orEmpty()
                saveWeather(Gson().toJson(weathers))
                weatherAdapter.submitList(weathers)
                Timber.d("Погода загружена из интернета")
            }

            override fun onFailure(call: Call<WeatherNW>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    fun saveWeather(list: String) {
        supportFragmentManager
            .beginTransaction()
            .add(weatherFragment, "weatherFragment")
            .commit()

        val bundle = Bundle()
        bundle.putString(KEY, list)
        weatherFragment.arguments = bundle
        Timber.d("Погода сохранена")


    }

    private fun loadFromFragment() {
        val weathers = Gson().fromJson<List<WeatherUI>>(
            weatherFragment.getWeather(),
            object : TypeToken<List<WeatherUI>>() {}.type
        ).toMutableList()
        weatherAdapter.submitList(weathers)
        Timber.d("Погода загружена из fraugment")
    }

    private fun initRecyclerView() {
        binding.recyclerViewWeather.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewWeather.adapter = weatherAdapter
    }
}
package com.example.weatherretrofit2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherretrofit2.data.WeatherApi
import com.example.weatherretrofit2.data.WeatherNW
import com.example.weatherretrofit2.data.toUI
import com.example.weatherretrofit2.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

private const val ID = "1503901"
private const val MEASUREMENT = "metric"
private const val API_KEY = "2ce0a504eccbb5cc5fdb54b14b60fab2"
private const val FRAGMENT_TAG = "weatherFragment"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherAdapter = WeatherAdapter()
    private val api: WeatherApi = WeatherApi.create()
    private lateinit var weatherFragment: WeatherFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRetainFragment()
        initRecyclerView()
        loadWeather()
    }

    private fun loadWeather() {
        if (weatherFragment.getWeather().isEmpty()) {
            loadFromNetwork()
        } else {
            loadFromStore()
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
                    weatherNw.toUI()
                }.orEmpty()
                saveWeather(weathers)
                weatherAdapter.submitList(weathers)
            }

            override fun onFailure(call: Call<WeatherNW>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    private fun initRetainFragment() {
        weatherFragment =
            (supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as WeatherFragment?)
                ?: WeatherFragment().also { fragment ->
                    supportFragmentManager
                        .beginTransaction()
                        .add(fragment, FRAGMENT_TAG)
                        .commit()
                }
    }

    fun saveWeather(list: List<WeatherUI>) {
        weatherFragment.setWeather(list)
    }

    private fun loadFromStore() {
        initRetainFragment()
        weatherAdapter.submitList(weatherFragment.getWeather())
    }

    private fun initRecyclerView() {
        binding.recyclerViewWeather.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewWeather.adapter = weatherAdapter
    }
}
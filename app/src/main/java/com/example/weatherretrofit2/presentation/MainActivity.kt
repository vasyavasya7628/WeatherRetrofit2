package com.example.weatherretrofit2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherretrofit2.data.WeatherApi
import com.example.weatherretrofit2.data.WeatherNW
import com.example.weatherretrofit2.data.WeatherUI
import com.example.weatherretrofit2.data.toDomain
import com.example.weatherretrofit2.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber


private const val ID = "1503901"
private const val MEASUREMENT = "metric"
private const val API_KEY = "2ce0a504eccbb5cc5fdb54b14b60fab2"
private const val KEY = "1"
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
        initRecyclerView()
        loadWeather()
    }

    private fun loadWeather() {
        if (supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) == null) {
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
                    weatherNw.toDomain()
                }.orEmpty()
                saveWeather(Gson().toJson(weathers))
                weatherAdapter.submitList(weathers)
            }

            override fun onFailure(call: Call<WeatherNW>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    fun saveWeather(list: String) {
        weatherFragment = WeatherFragment()
        supportFragmentManager
            .beginTransaction()
            .add(weatherFragment, FRAGMENT_TAG)
            .commit()

        val bundle = Bundle()
        bundle.putString(KEY, list)
        weatherFragment.arguments = bundle
    }

    private fun loadFromStore() {
        val gson = GsonBuilder()
            .serializeNulls()
            .create()
        weatherFragment = supportFragmentManager
            .findFragmentByTag(FRAGMENT_TAG) as WeatherFragment
        val weathers = gson.fromJson<List<WeatherUI>>(
            weatherFragment.savedList,
            object : TypeToken<List<WeatherUI>>() {}.type
        ).toMutableList()
        weatherAdapter.submitList(weathers)
    }

    private fun initRecyclerView() {
        binding.recyclerViewWeather.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewWeather.adapter = weatherAdapter
    }
}
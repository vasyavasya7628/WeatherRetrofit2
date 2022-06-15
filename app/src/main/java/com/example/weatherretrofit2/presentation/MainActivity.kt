package com.example.weatherretrofit2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherretrofit2.data.WeatherApi
import com.example.weatherretrofit2.data.WeatherFromNetwork
import com.example.weatherretrofit2.data.WeatherLocal
import com.example.weatherretrofit2.data.toDomain
import com.example.weatherretrofit2.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

private const val ID = "1503901"
private const val MEASUREMENT = "metric"
private const val API_KEY = "2ce0a504eccbb5cc5fdb54b14b60fab2"

class MainActivity : AppCompatActivity() {
    private val api: WeatherApi = WeatherApi.create()
    private lateinit var binding: ActivityMainBinding
    private var weatherAdapter = WeatherAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataNetwork()
        initRecyclerView()
    }

    private fun getDataNetwork() {
        api.getForecast(
            ID,
            MEASUREMENT,
            API_KEY
        ).enqueue(object : retrofit2.Callback<WeatherFromNetwork> {
            override fun onResponse(
                call: Call<WeatherFromNetwork>,
                response: Response<WeatherFromNetwork>
            ) {
                if (response.isSuccessful) {
                    val weather: WeatherFromNetwork = response.body() as WeatherFromNetwork
                    val weatherDomain: List<WeatherLocal> = weather.list.map { weatherNw ->
                        weatherNw.toDomain()
                    }

                    submitDataToAdapter(weatherDomain)
                } else Timber.d("ОТВЕТ", response.message())
            }

            override fun onFailure(call: Call<WeatherFromNetwork>, t: Throwable) {
                Timber.d("Ошибка подлючения или запрос был составлен не правильно")
            }
        })
    }

    private fun submitDataToAdapter(weatherLocal: List<WeatherLocal>) {
        weatherAdapter.submitList(weatherLocal)
    }

    private fun initRecyclerView() {
        binding.recyclerViewWeather.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewWeather.adapter = weatherAdapter
    }
}
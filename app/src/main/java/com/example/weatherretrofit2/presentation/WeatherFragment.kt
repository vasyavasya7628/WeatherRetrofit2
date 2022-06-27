package com.example.weatherretrofit2.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherretrofit2.data.WeatherApi
import com.example.weatherretrofit2.data.WeatherNW
import com.example.weatherretrofit2.data.WeatherStore
import com.example.weatherretrofit2.data.toDomain
import com.example.weatherretrofit2.databinding.FragmentWeatherBinding
import com.example.weatherretrofit2.ui.WeatherUI
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
private const val ID = "1503901"
private const val MEASUREMENT = "metric"
private const val API_KEY = "2ce0a504eccbb5cc5fdb54b14b60fab2"

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val weatherAdapter = WeatherAdapter()
    private val api: WeatherApi = WeatherApi.create()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        _binding = FragmentWeatherBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                        weatherNw.toDomain()
                    }.orEmpty()
                    WeatherStore.tempWeather = Gson().toJson(weathers)
                    weatherAdapter.submitList(weathers)
                } else Timber.d("ОТВЕТ", response.message())
            }

            override fun onFailure(call: Call<WeatherNW>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    private fun loadFromStore() {
        val weathers = Gson().fromJson<List<WeatherUI>>(
            WeatherStore.tempWeather,
            object : TypeToken<List<WeatherUI>>() {}.type
        ).toMutableList()
        weatherAdapter.submitList(weathers)
    }


    private fun initRecyclerView() {
        binding.recyclerViewWeather.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewWeather.adapter = weatherAdapter
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
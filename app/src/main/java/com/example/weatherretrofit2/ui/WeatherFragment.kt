package com.example.weatherretrofit2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherretrofit2.App
import com.example.weatherretrofit2.data.WeatherNW
import com.example.weatherretrofit2.data.toUi
import com.example.weatherretrofit2.databinding.FragmentWeatherBinding
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
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWeatherBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        loadWeather()
    }

    private fun loadWeather() {
        if (weatherViewModel.getWeather().isEmpty()) {
            loadFromNetwork()
        } else {
            loadFromStore()
        }
    }

    private fun loadFromStore() {
        weatherAdapter.submitList(weatherViewModel.getWeather())
    }

    private fun loadFromNetwork() {
        App().api.getForecast(
            ID,
            MEASUREMENT,
            API_KEY
        ).enqueue(object : retrofit2.Callback<WeatherNW> {
            override fun onResponse(
                call: Call<WeatherNW>,
                response: Response<WeatherNW>,
            ) {
                val weathers: List<WeatherUI> = response.body()?.list?.map { weatherNw ->
                    weatherNw.toUi()
                }.orEmpty()
                weatherViewModel.setWeather(weathers)
                weatherAdapter.submitList(weathers)
            }

            override fun onFailure(call: Call<WeatherNW>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    private fun initRecyclerView() {
        binding.recyclerViewWeather.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewWeather.adapter = weatherAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
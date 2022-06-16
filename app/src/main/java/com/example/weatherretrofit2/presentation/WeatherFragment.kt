package com.example.weatherretrofit2.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherretrofit2.data.WeatherApi
import com.example.weatherretrofit2.data.WeatherLocal
import com.example.weatherretrofit2.data.WeatherNetwork
import com.example.weatherretrofit2.data.toDomain
import com.example.weatherretrofit2.databinding.FragmentWeatherBinding
import com.example.weatherretrofit2.model.WeatherViewModel
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

private const val STATE_PLUS = 15 //сделал 15 градусов для проверки разных холдеров

private const val ID = "1503901"
private const val MEASUREMENT = "metric"
private const val API_KEY = "2ce0a504eccbb5cc5fdb54b14b60fab2"

class WeatherFragment : Fragment() {
    private val api: WeatherApi = WeatherApi.create()
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val weatherAdapter = WeatherAdapter()
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        if (savedInstanceState == null) {
            getDataFromNet()
        } else {
            submitDataToAdapter(weatherViewModel.weathers)
        }
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
                    weatherViewModel.setWeather(weatherDomain)
                    submitDataToAdapter(weatherDomain)
                } else Timber.d("ОТВЕТ", response.message())
            }

            override fun onFailure(call: Call<WeatherNetwork>, t: Throwable) {
                Timber.d("Ошибка подлючения или запрос был составлен не правильно")
            }
        })
    }

    private fun submitDataToAdapter(weatherLocal: List<WeatherLocal>) {
        val list = mutableListOf<DividerHolders>()
        weatherLocal.map { weather ->
            if (weather.temp > STATE_PLUS) {
                list.add(DividerHolders.WeatherPlus(weather))
            } else {
                list.add(DividerHolders.WeatherMinus(weather))
            }
        }

        weatherAdapter.submitList(list.toMutableList())
    }


    private fun initRecyclerView() {
        binding.recyclerViewWeather.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewWeather.adapter = weatherAdapter
    }

}
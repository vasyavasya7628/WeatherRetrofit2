package com.example.weatherretrofit2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherretrofit2.databinding.FragmentWeatherBinding
import com.example.weatherretrofit2.presentation.WeatherAdapter
import com.example.weatherretrofit2.ui.WeatherUI
import timber.log.Timber

private const val STATE_PLUS = 0
private const val KEY: String = "1"

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val weatherAdapter = WeatherAdapter()
    private var savedWeather: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        weatherViewModel.weatherExport.observe(viewLifecycleOwner) {
            loadDataToAdapter(it)
        }
        initRecyclerView()
    }

    private fun loadDataToAdapter(weatherDomain: List<WeatherUI>) {
        val list = mutableListOf<WeatherSealed>()
        weatherDomain.map { weather ->
            if (weather.temp > STATE_PLUS) {
                list.add(WeatherSealed.WeatherPlus(weather))
            } else {
                list.add(WeatherSealed.WeatherMinus(weather))
            }
        }
        savedWeather = weatherDomain.toString()
        Timber.d(list.toString())
        weatherAdapter.submitList(list.toMutableList())
    }


    private fun initRecyclerView() {
        binding.recyclerViewWeather.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewWeather.adapter = weatherAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
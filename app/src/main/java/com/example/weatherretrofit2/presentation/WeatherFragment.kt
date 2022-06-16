package com.example.weatherretrofit2.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherretrofit2.data.WeatherLocal
import com.example.weatherretrofit2.databinding.FragmentWeatherBinding
import com.example.weatherretrofit2.model.WeatherViewModel

private const val STATE_PLUS = 15 //сделал 15 градусов для проверки разных холдеров


class WeatherFragment : Fragment() {

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
        weatherViewModel.weatherExport.observe(viewLifecycleOwner) {
            submitDataToAdapter(it)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
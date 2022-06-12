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
import timber.log.Timber


class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val weatherAdapter = WeatherAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToViewModel()
        initRecyclerView()
    }

    private fun subscribeToViewModel() {
        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner) {
            submitDataToAdapter(it)
        }
    }

    private fun submitDataToAdapter(weatherLocal: List<WeatherLocal>) {

        weatherAdapter.submitList(weatherLocal.toMutableList())
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
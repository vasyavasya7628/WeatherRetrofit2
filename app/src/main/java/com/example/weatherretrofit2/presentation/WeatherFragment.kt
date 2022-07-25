package com.example.weatherretrofit2.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherretrofit2.databinding.FragmentWeatherBinding
import timber.log.Timber

private const val KEY = "1"

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private var savedList: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWeatherBinding.inflate(layoutInflater, container, false)
        savedList = arguments?.getString(KEY)
        return binding.root
    }

    fun getWeather(): String? {
        return savedList
    }

}
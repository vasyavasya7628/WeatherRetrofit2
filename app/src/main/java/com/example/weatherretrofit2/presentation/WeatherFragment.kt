package com.example.weatherretrofit2.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment

private const val KEY = "1"

class WeatherFragment : Fragment() {
    var savedList: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        savedList = arguments?.getString(KEY)
    }
}
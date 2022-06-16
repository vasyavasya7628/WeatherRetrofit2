package com.example.weatherretrofit2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherretrofit2.R
import com.example.weatherretrofit2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var weatherFragment: WeatherFragment = WeatherFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentWeather, weatherFragment)
                .commit()
        }
    }
}


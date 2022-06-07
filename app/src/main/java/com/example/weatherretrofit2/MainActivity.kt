package com.example.weatherretrofit2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherretrofit2.databinding.ActivityMainBinding
import okhttp3.logging.HttpLoggingInterceptor

//https://api.openweathermap.org/data/2.5/forecast?id=1503901&appid=2ce0a504eccbb5cc5fdb54b14b60fab2 для Api
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
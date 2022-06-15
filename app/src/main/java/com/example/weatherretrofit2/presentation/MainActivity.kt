package com.example.weatherretrofit2.presentation

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherretrofit2.data.WeatherApi
import com.example.weatherretrofit2.data.WeatherLocal
import com.example.weatherretrofit2.data.WeatherNetwork
import com.example.weatherretrofit2.data.toDomain
import com.example.weatherretrofit2.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber

private const val STATE_PLUS = 15 //сделал 15 градусов для проверки разных холдеров
private const val ID = "1503901"
private const val MEASUREMENT = "metric"
private const val API_KEY = "2ce0a504eccbb5cc5fdb54b14b60fab2"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherAdapter = WeatherAdapter()
    private val api: WeatherApi = WeatherApi.create()
    private var tempWeather: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null){
            getDataFromNet()
        }else Timber.d("Произошел перевот экрана, данные восстановлены из переменной")

        initRecyclerView()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val gson = Gson()
        val weatherDomain = gson.fromJson<List<WeatherLocal>>(
            savedInstanceState.getString("1", ""),
            object : TypeToken<List<WeatherLocal>>() {}.type
        )
        submitDataToAdapter(weatherDomain)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("1", tempWeather)
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
                    tempWeather = Gson().toJson(weatherDomain)

                    Timber.tag("!Данные загружены в tempWeather!").d(tempWeather)
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
        binding.recyclerViewWeather.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewWeather.adapter = weatherAdapter
    }
}
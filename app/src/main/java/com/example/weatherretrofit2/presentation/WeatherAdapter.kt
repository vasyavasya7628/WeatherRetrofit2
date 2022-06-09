package com.example.weatherretrofit2.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherretrofit2.data.DataWeather
import com.example.weatherretrofit2.databinding.ItemWeatherPlusBinding
import java.text.SimpleDateFormat
import java.util.*


sealed class WeatherSealed {
    data class LoadedWeather(
        val loadedWeather: DataWeather) : WeatherSealed()
}

class WeatherAdapter : ListAdapter<WeatherSealed, RecyclerView.ViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is WeatherSealed.LoadedWeather -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> Holder(
                ItemWeatherPlusBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is Holder -> holder.bind(item as WeatherSealed.LoadedWeather)
            else -> throw error("error")
        }
    }


}

class Holder(binding: ItemWeatherPlusBinding) : RecyclerView.ViewHolder(binding.root) {
    private val weatherText = binding.textWeather
    private val weatherTemp = binding.tempWeather
    private val weatherIcon = binding.iconWeather

    fun bind(item: WeatherSealed.LoadedWeather) {
        weatherTemp.text = item.loadedWeather.temp.toString()
        weatherText.text = getDateTime(item.loadedWeather.dt.toString())
        Glide.with(weatherIcon.context)
            .load("https://openweathermap.org/img/wn/${item.loadedWeather.icon}.png")
            .into(weatherIcon)
    }
}

private fun getDateTime(s: String): String? {

    val sdf = SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.getDefault())
    val netDate = Date(s.toLong() * 1000)
    return sdf.format(netDate.time)
}

private val diffUtil = object : DiffUtil.ItemCallback<WeatherSealed>() {

    override fun areItemsTheSame(oldItem: WeatherSealed, newItem: WeatherSealed): Boolean {
            oldItem as WeatherSealed.LoadedWeather
            newItem as WeatherSealed.LoadedWeather
            return oldItem.loadedWeather.temp == newItem.loadedWeather.temp
    }

    override fun areContentsTheSame(oldItem: WeatherSealed, newItem: WeatherSealed): Boolean {
        return oldItem == newItem
    }
}

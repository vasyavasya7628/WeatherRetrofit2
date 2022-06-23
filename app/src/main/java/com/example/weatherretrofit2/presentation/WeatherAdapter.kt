package com.example.weatherretrofit2.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherretrofit2.databinding.ItemWeatherBinding
import com.example.weatherretrofit2.presentation.model.WeatherUI
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class WeatherAdapter : ListAdapter<WeatherUI, WeatherViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            ItemWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(getItem(position) as WeatherUI)
    }

}

class WeatherViewHolder(private val binding: ItemWeatherBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WeatherUI) {
        binding.tempWeather.text = item.temp.toString()
        binding.textWeather.text = getDateTime(item.dt.toString())

        Glide.with(binding.iconWeather.context)
            .load("https://openweathermap.org/img/wn/${item.icon}.png")
            .into(binding.iconWeather)
    }
}

private fun getDateTime(s: String): String? {

    val triggerTime =
        LocalDateTime.ofInstant(
            Instant.ofEpochSecond(
                s.toLong()
            ),
            TimeZone.getDefault().toZoneId()
        )
    return DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss").format(triggerTime)
}

private val diffUtil = object : DiffUtil.ItemCallback<WeatherUI>() {

    override fun areItemsTheSame(oldItem: WeatherUI, newItem: WeatherUI): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WeatherUI, newItem: WeatherUI): Boolean {
        return oldItem == newItem
    }
}

package com.example.weatherretrofit2.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherretrofit2.data.WeatherLocal
import com.example.weatherretrofit2.databinding.ItemWeatherPlusBinding
import java.text.SimpleDateFormat
import java.util.*


class WeatherAdapter : ListAdapter<WeatherLocal, ItemViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemWeatherPlusBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position) as WeatherLocal)
    }

}

class ItemViewHolder(binding: ItemWeatherPlusBinding) : RecyclerView.ViewHolder(binding.root) {
    private val bindItem = binding
    fun bind(item: WeatherLocal) {
        bindItem.tempWeather.text = item.temp.toString()
        bindItem.textWeather.text = getDateTime(item.dt.toString())

        Glide.with(bindItem.iconWeather.context)
            .load("https://openweathermap.org/img/wn/${item.icon}.png")
            .into(bindItem.iconWeather)
    }
}

private fun getDateTime(s: String): String? {

    val sdf = SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.getDefault())
    val netDate = Date(s.toLong() * 1000)
    return sdf.format(netDate.time)
}

private val diffUtil = object : DiffUtil.ItemCallback<WeatherLocal>() {

    override fun areItemsTheSame(oldItem: WeatherLocal, newItem: WeatherLocal): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WeatherLocal, newItem: WeatherLocal): Boolean {
        return oldItem == newItem
    }
}

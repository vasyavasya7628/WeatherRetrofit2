package com.example.weatherretrofit2.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherretrofit2.databinding.ItemWeatherMinusBinding
import com.example.weatherretrofit2.databinding.ItemWeatherPlusBinding
import java.text.SimpleDateFormat
import java.util.*


sealed class WeatherSealed {

    data class WeatherPlus(val weatherPlus: DataWeather) : WeatherSealed()
    data class WeatherMinus(val weatherMinus: DataWeather) : WeatherSealed()
}

class WeatherAdapter : ListAdapter<WeatherSealed, RecyclerView.ViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is WeatherSealed.WeatherMinus -> 0
            is WeatherSealed.WeatherPlus -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> HolderTempMinus(
                ItemWeatherMinusBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            1 -> HolderTempPlus(
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
            is HolderTempMinus -> holder.bind(item as WeatherSealed.WeatherMinus)
            is HolderTempPlus -> holder.bind(item as WeatherSealed.WeatherPlus)
            else -> throw error("error")
        }
    }


}

class HolderTempPlus(binding: ItemWeatherPlusBinding) : RecyclerView.ViewHolder(binding.root) {
    private val weatherDateView = binding.dataWeateherPlus
    private val weatherPlus = binding.tempPlus
    private val iconPlus = binding.iconWeatherPlus

    fun bind(item: WeatherSealed.WeatherPlus) {
        weatherPlus.text = item.weatherPlus.temp.toString()
        weatherDateView.text = getDateTime(item.weatherPlus.dt.toString())
        Glide.with(iconPlus.context)
            .load("https://openweathermap.org/img/wn/${item.weatherPlus.icon}.png")
            .into(iconPlus)
    }
}

class HolderTempMinus(binding: ItemWeatherMinusBinding) : RecyclerView.ViewHolder(binding.root) {
    private val weatherDateView = binding.dataWeateherMinus
    private val weatherMinus = binding.tempMinus
    private val iconMinus = binding.iconWeatherMinus

    fun bind(item: WeatherSealed.WeatherMinus) {
        weatherMinus.text = item.weatherMinus.temp.toString()
        weatherDateView.text = getDateTime(item.weatherMinus.dt.toString())
        Glide.with(iconMinus.context)
            .load("https://openweathermap.org/img/wn/${item.weatherMinus.icon}.png")
            .into(iconMinus)
    }
}


private fun getDateTime(s: String): String? {

    val sdf = SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.getDefault())
    val netDate = Date(s.toLong() * 1000)
    return sdf.format(netDate.time)
}

private val diffUtil = object : DiffUtil.ItemCallback<WeatherSealed>() {

    override fun areItemsTheSame(oldItem: WeatherSealed, newItem: WeatherSealed): Boolean {
        val isWeatherPlus: Boolean =
            oldItem is WeatherSealed.WeatherPlus && newItem is WeatherSealed.WeatherPlus

        if (isWeatherPlus) {
            oldItem as WeatherSealed.WeatherPlus
            newItem as WeatherSealed.WeatherPlus
            return oldItem.weatherPlus.temp == newItem.weatherPlus.temp
        }

        val isWeatherMinus: Boolean =
            oldItem is WeatherSealed.WeatherMinus && newItem is WeatherSealed.WeatherMinus
        if (isWeatherMinus) {
            oldItem as WeatherSealed.WeatherMinus
            newItem as WeatherSealed.WeatherMinus
            return oldItem.weatherMinus.temp == newItem.weatherMinus.temp
        }

        return false
    }

    override fun areContentsTheSame(oldItem: WeatherSealed, newItem: WeatherSealed): Boolean {
        return oldItem == newItem
    }
}

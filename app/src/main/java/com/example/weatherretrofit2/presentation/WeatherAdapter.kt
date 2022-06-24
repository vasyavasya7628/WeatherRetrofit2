package com.example.weatherretrofit2.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherretrofit2.ui.WeatherUI
import com.example.weatherretrofit2.databinding.ItemWeatherMinusBinding
import com.example.weatherretrofit2.databinding.ItemWeatherPlusBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


sealed class DividerHolders {
    data class WeatherPlus(val weatherPlus: WeatherUI) : DividerHolders()
    data class WeatherMinus(val weatherMinus: WeatherUI) : DividerHolders()
}

class WeatherAdapter : ListAdapter<DividerHolders, RecyclerView.ViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DividerHolders.WeatherMinus -> 0
            is DividerHolders.WeatherPlus -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> MinusViewHolder(
                ItemWeatherMinusBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            1 -> PlusViewHolder(
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
            is MinusViewHolder -> holder.bind(item as DividerHolders.WeatherMinus)
            is PlusViewHolder -> holder.bind(item as DividerHolders.WeatherPlus)
        }
    }
}

class PlusViewHolder(private val binding: ItemWeatherPlusBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DividerHolders.WeatherPlus) {
        binding.tempPlus.text = item.weatherPlus.temp.toString()
        binding.dataWeateherPlus.text = getDateTime(item.weatherPlus.dt.toString())
        Glide.with(binding.iconWeatherPlus.context)
            .load("https://openweathermap.org/img/wn/${item.weatherPlus.icon}.png")
            .into(binding.iconWeatherPlus)
    }
}

class MinusViewHolder(private val binding: ItemWeatherMinusBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DividerHolders.WeatherMinus) {
        binding.tempMinus.text = item.weatherMinus.temp.toString()
        binding.dataWeateherMinus.text = getDateTime(item.weatherMinus.dt.toString())
        Glide.with(binding.iconWeatherMinus.context)
            .load("https://openweathermap.org/img/wn/${item.weatherMinus.icon}.png")
            .into(binding.iconWeatherMinus)
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

private val diffUtil = object : DiffUtil.ItemCallback<DividerHolders>() {

    override fun areItemsTheSame(oldItem: DividerHolders, newItem: DividerHolders): Boolean {
        val isWeatherPlus: Boolean =
            oldItem is DividerHolders.WeatherPlus && newItem is DividerHolders.WeatherPlus

        if (isWeatherPlus) {
            oldItem as DividerHolders.WeatherPlus
            newItem as DividerHolders.WeatherPlus
            return oldItem.weatherPlus.temp == newItem.weatherPlus.temp
        }

        val isWeatherMinus: Boolean =
            oldItem is DividerHolders.WeatherMinus && newItem is DividerHolders.WeatherMinus
        if (isWeatherMinus) {
            oldItem as DividerHolders.WeatherMinus
            newItem as DividerHolders.WeatherMinus
            return oldItem.weatherMinus.temp == newItem.weatherMinus.temp
        }

        return false
    }

    override fun areContentsTheSame(oldItem: DividerHolders, newItem: DividerHolders): Boolean {
        return oldItem == newItem
    }
}
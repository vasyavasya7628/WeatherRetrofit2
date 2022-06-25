package com.example.weatherretrofit2.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherretrofit2.databinding.ItemWeatherMinusBinding
import com.example.weatherretrofit2.databinding.ItemWeatherPlusBinding
import com.example.weatherretrofit2.ui.WeatherUI
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val TYPE_PLUS = 1
private const val TYPE_MINUS = 0


class WeatherAdapter : ListAdapter<WeatherUI, RecyclerView.ViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int): Int = if (getItem(position).temp > 15) {
        TYPE_PLUS
    } else {
        TYPE_MINUS
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MINUS -> MinusViewHolder(
                ItemWeatherMinusBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            TYPE_PLUS -> PlusViewHolder(
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
        when (getItemViewType(position)) {
            TYPE_MINUS -> (holder as MinusViewHolder).bind(item)
            TYPE_PLUS  -> (holder as PlusViewHolder).bind(item)
        }
    }
}

class PlusViewHolder(private val binding: ItemWeatherPlusBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WeatherUI) {
        binding.tempPlus.text = item.temp.toString()
        binding.dataWeateherPlus.text = getDateTime(item.dt.toString())
        Glide.with(binding.iconWeatherPlus.context)
            .load("https://openweathermap.org/img/wn/${item.icon}.png")
            .into(binding.iconWeatherPlus)
    }
}

class MinusViewHolder(private val binding: ItemWeatherMinusBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WeatherUI) {
        binding.tempMinus.text = item.temp.toString()
        binding.dataWeateherMinus.text = getDateTime(item.dt.toString())
        Glide.with(binding.iconWeatherMinus.context)
            .load("https://openweathermap.org/img/wn/${item.icon}.png")
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

private val diffUtil = object : DiffUtil.ItemCallback<WeatherUI>() {

    override fun areItemsTheSame(oldItem: WeatherUI, newItem: WeatherUI): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WeatherUI, newItem: WeatherUI): Boolean {
        return oldItem == newItem
    }
}
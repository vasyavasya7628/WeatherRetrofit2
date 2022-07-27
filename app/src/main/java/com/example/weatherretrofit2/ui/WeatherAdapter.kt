package com.example.weatherretrofit2.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherretrofit2.R
import com.example.weatherretrofit2.databinding.ItemWeatherMinusBinding
import com.example.weatherretrofit2.databinding.ItemWeatherPlusBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val TYPE_PLUS = 1
private const val TYPE_MINUS = 0
private const val TIME_PATTERN = "yyyy-MM-dd  HH:mm:ss"
private const val ICON_PATH = "https://openweathermap.org/img/wn/"
private const val PNG_STRING = ".png"
private const val LOWER_TEMP = 15

class WeatherAdapter : ListAdapter<WeatherUI, RecyclerView.ViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int) = if (getItem(position).temp > LOWER_TEMP) {
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
            TYPE_PLUS -> (holder as PlusViewHolder).bind(item)
        }
    }
}

class PlusViewHolder(private val binding: ItemWeatherPlusBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WeatherUI) {
        binding.tvTempP.text = binding.root.context.getString(R.string.tempCelsius, item.temp)
        binding.tvDataP.text = getDateTime(item.dt.toString())
        Glide.with(binding.ivIconP.context)
            .load("$ICON_PATH${item.icon}$PNG_STRING")
            .into(binding.ivIconP)
    }
}

class MinusViewHolder(private val binding: ItemWeatherMinusBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WeatherUI) {
        binding.tvTempM.text = binding.root.context.getString(R.string.tempCelsius, item.temp)
        binding.tvData.text = getDateTime(item.dt.toString())
        Glide.with(binding.ivIconM.context)
            .load("$ICON_PATH${item.icon}$PNG_STRING")
            .into(binding.ivIconM)
    }
}

private fun getDateTime(time: String): String? {

    val triggerTime =
        LocalDateTime.ofInstant(
            Instant.ofEpochSecond(
                time.toLong()
            ),
            TimeZone.getDefault().toZoneId()
        )
    return DateTimeFormatter.ofPattern(TIME_PATTERN).format(triggerTime)
}

private val diffUtil = object : DiffUtil.ItemCallback<WeatherUI>() {
    override fun areItemsTheSame(oldItem: WeatherUI, newItem: WeatherUI): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WeatherUI, newItem: WeatherUI): Boolean {
        return oldItem == newItem
    }
}
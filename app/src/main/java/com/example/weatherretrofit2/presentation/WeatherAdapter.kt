package com.example.weatherretrofit2.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherretrofit2.data.WeatherUI
import com.example.weatherretrofit2.databinding.ItemWeatherMinusBinding
import com.example.weatherretrofit2.databinding.ItemWeatherPlusBinding
import java.text.SimpleDateFormat
import java.util.*


sealed class DividerViewHolder {

    data class WeatherPlus(val weatherPlus: WeatherUI) : DividerViewHolder()
    data class WeatherMinus(val weatherMinus: WeatherUI) : DividerViewHolder()
}

class WeatherAdapter : ListAdapter<DividerViewHolder, RecyclerView.ViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DividerViewHolder.WeatherMinus -> 0
            is DividerViewHolder.WeatherPlus -> 1
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
            is MinusViewHolder -> holder.bind(item as DividerViewHolder.WeatherMinus)
            is PlusViewHolder -> holder.bind(item as DividerViewHolder.WeatherPlus)
        }
    }
}

class PlusViewHolder(private val binding: ItemWeatherPlusBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DividerViewHolder.WeatherPlus) {
        binding.tempPlus.text = item.weatherPlus.temp.toString()
        binding.dataWeateherPlus.text = getDateTime(item.weatherPlus.dt.toString())
        Glide.with(binding.iconWeatherPlus.context)
            .load("https://openweathermap.org/img/wn/${item.weatherPlus.icon}.png")
            .into(binding.iconWeatherPlus)
    }
}

class MinusViewHolder(private val binding: ItemWeatherMinusBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DividerViewHolder.WeatherMinus) {
        binding.tempMinus.text = item.weatherMinus.temp.toString()
        binding.dataWeateherMinus.text = getDateTime(item.weatherMinus.dt.toString())
        Glide.with(binding.iconWeatherMinus.context)
            .load("https://openweathermap.org/img/wn/${item.weatherMinus.icon}.png")
            .into(binding.iconWeatherMinus)
    }
}

private fun getDateTime(s: String): String? {

    val sdf = SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.getDefault())
    val netDate = Date(s.toLong() * 1000)
    return sdf.format(netDate.time)
}

private val diffUtil = object : DiffUtil.ItemCallback<DividerViewHolder>() {

    override fun areItemsTheSame(oldItem: DividerViewHolder, newItem: DividerViewHolder): Boolean {
        val isWeatherPlus: Boolean =
            oldItem is DividerViewHolder.WeatherPlus && newItem is DividerViewHolder.WeatherPlus

        if (isWeatherPlus) {
            oldItem as DividerViewHolder.WeatherPlus
            newItem as DividerViewHolder.WeatherPlus
            return oldItem.weatherPlus.temp == newItem.weatherPlus.temp
        }

        val isWeatherMinus: Boolean =
            oldItem is DividerViewHolder.WeatherMinus && newItem is DividerViewHolder.WeatherMinus
        if (isWeatherMinus) {
            oldItem as DividerViewHolder.WeatherMinus
            newItem as DividerViewHolder.WeatherMinus
            return oldItem.weatherMinus.temp == newItem.weatherMinus.temp
        }

        return false
    }

    override fun areContentsTheSame(oldItem: DividerViewHolder, newItem: DividerViewHolder): Boolean {
        return oldItem == newItem
    }
}
package com.lucas.weatherapi.data.model

data class WeatherResponse(
    val city_name: String,
    val country_code: String,
    val lat: Float,
    val lon: Float,
    val timezone: String,
    val data: List<ForecastDay>
)

data class ForecastDay(
    val valid_date: String,
    val max_temp: Float,
    val min_temp: Float,
    val temp: Float,
    val sunrise_ts: Long,
    val sunset_ts: Long,
    val wind_spd: Float,
    val wind_dir: Int,
    val rh: Int,
    val pop: Int,
    val weather: Condition,
    val clouds: Int
)

data class Condition(
    val description: String,
    val icon: String
)

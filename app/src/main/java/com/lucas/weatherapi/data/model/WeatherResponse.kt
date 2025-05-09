package com.lucas.weatherapi.data.model

import java.time.LocalTime

data class WeatherResponse(
    val location: Location,
    val current: Current,
    val forecast: Forecast
)

data class Location(
    val name: String,
    val country: String,
    val lat: Float,
    val long: Float,
    val localTime: String
)

data class Current(
    val last_updated: String,
    val temp_c: Float,
    val temp_f: Float
)

data class Forecast(
    val forecast_day: List<ForecastDay>
)

data class ForecastDay(
    val day: Day
)

data class Day(
    val maxtemp_c: Float,
    val mintemp_c: Float,
    val maxtemp_f: Float,
    val mintemp_f: Float
)
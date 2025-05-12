package com.lucas.weatherapi.data.model

data class WeatherResponse(
    val location: Location,
    val current: Current,
    val forecast: Forecast
)

data class Location(
    val name: String,
    val country: String,
    val lat: Float,
    val lon: Float,
    val localTime: String
)

data class Current(
    val last_updated: String,
    val temp_c: Float,
    val temp_f: Float,
    val condition: Condition,
    val wind_kph: Float,
    val humidity: Float,

)

data class Forecast(
    val date: String,
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val day: Day
)

data class Day(
    val maxtemp_c: Float,
    val mintemp_c: Float,
    val maxtemp_f: Float,
    val mintemp_f: Float,
    val daily_chance_of_rain: Float,
    val condition: Condition,
    val avghumidity: Int,
    val maxwind_kph: Float
)

data class Condition(
    val text: String,
    val icon: String
)
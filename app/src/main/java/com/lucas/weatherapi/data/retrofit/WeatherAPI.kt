package com.lucas.weatherapi.data.retrofit

import com.lucas.weatherapi.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    // Recurso que queremos consultar
    @GET("forecast.json")
    // Metodo para obtener los datos esperados de la API
    suspend fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 8,
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no"
    ): WeatherResponse
}
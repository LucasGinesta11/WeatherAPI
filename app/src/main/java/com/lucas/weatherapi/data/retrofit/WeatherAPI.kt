package com.lucas.weatherapi.data.retrofit

import com.lucas.weatherapi.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    // Recurso que queremos consultar
    @GET("forecast/daily")
    // Metodo para obtener los datos esperados de la API
    suspend fun getForecast(
        @Query("city") city: String,
        @Query("days") days: Int = 5,
        @Query("key") apiKey: String,
    ): WeatherResponse
}
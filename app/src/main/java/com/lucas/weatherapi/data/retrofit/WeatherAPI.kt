package com.lucas.weatherapi.data.retrofit

import com.lucas.weatherapi.data.model.CurrentResponse
import com.lucas.weatherapi.data.model.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    // Recurso que queremos consultar
    @GET("forecast/daily")
    // Metodo para obtener los datos esperados de la API
    suspend fun getForecast(
        @Query("city") city: String,
        @Query("lang") lang :String,
        @Query("days") days: Int,
        @Query("key") apiKey: String,
    ): ForecastResponse

    // Recurso que queremos consultar
    @GET("current")
    // Metodo para obtener los datos esperados de la API
    suspend fun getCurrent(
        @Query("city") city: String,
        @Query("lang") lang :String,
        @Query("key") apiKey: String,
    ): CurrentResponse
}
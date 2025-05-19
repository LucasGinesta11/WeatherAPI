package com.lucas.weatherapi.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.weatherbit.io/v2.0/"
    const val API_KEY = "3cd8b92528154d97ac76b917d315cf81"
    const val days = 8
    const val city = "Burriana"

    val api: WeatherAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherAPI::class.java)
    }
}
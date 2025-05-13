package com.lucas.weatherapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.lucas.weatherapi.ui.WeatherScreen
import com.lucas.weatherapi.viewModel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa ViewModel
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        weatherViewModel.getWeatherForecast(
            city = "Burriana",
            days = 5,
            apiKey = "3cd8b92528154d97ac76b917d315cf81"

        )
        enableEdgeToEdge()
        setContent {
            WeatherScreen(viewModel = weatherViewModel)
        }
    }
}


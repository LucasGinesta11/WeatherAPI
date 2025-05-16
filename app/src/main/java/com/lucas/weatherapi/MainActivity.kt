package com.lucas.weatherapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.lucas.weatherapi.ui.WeatherScreen
import com.lucas.weatherapi.viewModel.WeatherViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa ViewModel
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        val languageCode = Locale.getDefault().language

        weatherViewModel.getWeatherForecast(
            city = "Burriana",
            lang = languageCode,
            days = 8,
            apiKey = "3cd8b92528154d97ac76b917d315cf81"
        )

        weatherViewModel.getWeatherCurrent(
            city = "Burriana",
            lang = languageCode,
            apiKey = "3cd8b92528154d97ac76b917d315cf81"
        )

        enableEdgeToEdge()
        setContent {
            WeatherScreen(viewModel = weatherViewModel)
        }
    }
}


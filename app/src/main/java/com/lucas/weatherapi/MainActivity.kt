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

        // Observa los cambios
        weatherViewModel.weatherData.observe(this) { weatherResponse ->
            // Actualizar datos
        }

        weatherViewModel.getWeatherForecast(
            apiKey = "077486a2e1f44c3185c72052250905",
            location = "Rob",
            days = 3
        )
        enableEdgeToEdge()
        setContent {
            WeatherScreen(viewModel = weatherViewModel)
        }
    }
}


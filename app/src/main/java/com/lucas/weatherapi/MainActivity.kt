package com.lucas.weatherapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.lucas.weatherapi.data.retrofit.RetrofitInstance
import com.lucas.weatherapi.ui.screen.WeatherScreen
import com.lucas.weatherapi.ui.viewModel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa ViewModel
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        val languageCode = Locale.getDefault().language

        lifecycleScope.launch(Dispatchers.IO) {

            val a = async {
                weatherViewModel.getWeatherForecast(
                    city = RetrofitInstance.city,
                    lang = languageCode,
                    days = RetrofitInstance.days,
                    apiKey = RetrofitInstance.API_KEY
                )

                weatherViewModel.getWeatherCurrent(
                    city = RetrofitInstance.city,
                    lang = languageCode,
                    apiKey = RetrofitInstance.API_KEY
                )
            }
        }

        enableEdgeToEdge()
        setContent {
            WeatherScreen(viewModel = weatherViewModel)
        }
    }
}


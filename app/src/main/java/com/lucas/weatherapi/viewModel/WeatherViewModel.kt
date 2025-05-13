package com.lucas.weatherapi.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucas.weatherapi.data.model.WeatherResponse
import com.lucas.weatherapi.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    // LiveData para almacenar el estado de la UI, y puedan ser cambiados los datos
    val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse?> get() = _weatherData

    // Obtener el pronostico del tiempo
    fun getWeatherForecast(city: String, days: Int = 5, apiKey: String) {
        // Llamada a api desde corrutina
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getForecast(city, days, apiKey)

                // Actualizar LiveData
                _weatherData.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
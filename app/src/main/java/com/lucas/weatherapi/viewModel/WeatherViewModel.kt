package com.lucas.weatherapi.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucas.weatherapi.data.model.WeatherResponse
import com.lucas.weatherapi.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    // LiveData para almacenar el estado de la UI, y puedan ser cambiados los datos
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse?> get() = _weatherData

    // Obtener el pronostico del tiempo
    fun getWeatherForecast(apiKey: String, location: String, days: Int = 8){
        // Llamada a api desde corrutina
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getForecast(apiKey, location, days)

                // Actualizar LiveData
                _weatherData.postValue(response)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}
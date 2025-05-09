package com.lucas.weatherapi.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucas.weatherapi.data.model.WeatherResponse
import com.lucas.weatherapi.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    // LiveData para almacenar el estado de la UI
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse?> get() = _weatherData

//    private val _error = MutableLiveData<String?>()
//    val error: LiveData<String?> get() = _error

    // Obtener el pronostico del tiempo
    fun getWeatherForecast(apiKey: String, location: String, days: Int = 3){
        // Llamada a api desde corrutina
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getForecast(apiKey, location, days)

                // Actualizar LiveData
                _weatherData.postValue(response)
//                _error.postValue(null)
            }
            catch (e: Exception){
                e.printStackTrace()
//                _error.postValue("Error en la solicitud: ${e.message}")
            }
            catch (e: Exception){
                e.printStackTrace()
//                _error.postValue("Error desconocido: ${e.message}")
            }
        }
    }
}
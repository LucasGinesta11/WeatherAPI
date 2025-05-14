package com.lucas.weatherapi.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucas.weatherapi.data.model.CurrentResponse
import com.lucas.weatherapi.data.model.ForecastResponse
import com.lucas.weatherapi.data.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    // LiveData para almacenar el estado de la UI, y puedan ser cambiados los datos
    val _forecastData = MutableLiveData<ForecastResponse>()
    val forecastData: LiveData<ForecastResponse?> get() = _forecastData

    val _currentData = MutableLiveData<CurrentResponse>()
    val currentData: LiveData<CurrentResponse?> get() = _currentData

    val _searchCities = MutableLiveData<List<Pair<String, String>>>()
    val searchCities: LiveData<List<Pair<String, String>>> = _searchCities

    // Obtener el pronostico del tiempo
    fun getWeatherForecast(city: String, lang: String, days: Int, apiKey: String) {
        // Llamada a api desde corrutina
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getForecast(city, lang, days, apiKey)

                // Actualizar LiveData
                _forecastData.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Obtener el pronostico del tiempo actual
    fun getWeatherCurrent(city: String, lang: String, apiKey: String) {
        // Llamada a api desde corrutina
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCurrent(city, lang, apiKey)

                // Actualizar LiveData
                _currentData.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Busqueda
    fun searchCities(query: String, lang: String, apiKey: String){
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getForecast(query, lang, 5, apiKey)
                val suggestion = Pair(response.city_name, response.country_code)
                _searchCities.postValue(listOf(suggestion))
            }catch(e: Exception){
                _searchCities.postValue(emptyList())
                e.printStackTrace()
            }
        }
    }
}
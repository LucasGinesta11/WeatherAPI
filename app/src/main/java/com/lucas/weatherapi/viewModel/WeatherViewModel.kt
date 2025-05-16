package com.lucas.weatherapi.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucas.weatherapi.data.model.CityResponse
import com.lucas.weatherapi.data.model.CurrentResponse
import com.lucas.weatherapi.data.model.ForecastResponse
import com.lucas.weatherapi.data.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.Query

class WeatherViewModel : ViewModel() {
    // LiveData para almacenar el estado de la UI, y puedan ser cambiados los datos
    val _forecastData = MutableLiveData<ForecastResponse>()
    val forecastData: LiveData<ForecastResponse?> get() = _forecastData

    val _currentData = MutableLiveData<CurrentResponse>()
    val currentData: LiveData<CurrentResponse?> get() = _currentData

    val _searchCities = MutableLiveData<List<Pair<String, String>>>()
    val searchCities: LiveData<List<Pair<String, String>>> = _searchCities

    val allCities = mutableListOf<CityResponse>()

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

    fun loadCities(context: Context){
        viewModelScope.launch (Dispatchers.IO){
            try{
                val inputStream = context.assets.open("cities.csv")
                inputStream.bufferedReader().useLines { lines ->
                    lines.drop(1).forEach { line ->
                        val parts = line.split(",")
                        if(parts.size >= 2){
                            val city = CityResponse(parts[0], parts[1])
                            allCities.add(city)
                        }
                    }
                }
            }
            catch(e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun searchCities(query: String){
        viewModelScope.launch {
            val lowerQuery = query.lowercase()

            val suggestions = allCities
                .filter { it.city_name.lowercase().startsWith(lowerQuery) }
                .distinctBy { it.city_name to it.country_code }
                .take(3)
                .map { it.city_name to it.country_code }

            _searchCities.postValue(suggestions)
        }
    }
}
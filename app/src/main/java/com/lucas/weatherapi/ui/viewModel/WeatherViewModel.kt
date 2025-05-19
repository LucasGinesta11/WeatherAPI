package com.lucas.weatherapi.ui.viewModel

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

class WeatherViewModel : ViewModel() {

    // LiveData para almacenar pronosticos, y puedan ser cambiados los datos
    val _forecastData = MutableLiveData<ForecastResponse>()
    val forecastData: LiveData<ForecastResponse?> get() = _forecastData

    // LiveData para almacenar tiempo actual
    val _currentData = MutableLiveData<CurrentResponse>()
    val currentData: LiveData<CurrentResponse?> get() = _currentData

    // LiveData de ciudades
    val allCities = mutableListOf<CityResponse>()
    val _citiesLoaded = MutableLiveData<Boolean>()

    // LiveData para almacenar busquedas
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

    // Accede a assets donde esta el csv, lo lee y obtiene el primer y segundo valor de cada ciudad
    fun loadCities(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.assets.open("cities.csv")
                inputStream.bufferedReader().useLines { lines ->
                    // Salta la linea 1 (encabezado)
                    lines.drop(1).forEach { line ->
                        val parts = line.split(",")
                        val city = CityResponse(parts[1].trim(), parts[3].trim())
                        allCities.add(city)
                    }
                }
                _citiesLoaded.postValue(true)
            } catch (e: Exception) {
                _citiesLoaded.postValue(false)
            }
        }
    }

    // Buscar sugerencias de las ciudades a partir de sus dos atributos
    fun searchCities(query: String) {
        viewModelScope.launch {
            val lowerQuery = query.lowercase().trim()

            val suggestions = allCities
                .filter { "${it.city_name}, ${it.country_code}".lowercase().startsWith(lowerQuery) }
                .distinctBy { it.city_name to it.country_code }
                .take(allCities.size)
                .map { it.city_name to it.country_code }

            _searchCities.postValue(suggestions)
        }
    }

    // Limpia las sugerencias cuando se selecciona una localidad
    fun clearSuggestions() {
        _searchCities.postValue(emptyList())
    }
}
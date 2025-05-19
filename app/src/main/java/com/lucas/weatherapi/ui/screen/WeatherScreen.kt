package com.lucas.weatherapi.ui.screen

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucas.weatherapi.data.retrofit.RetrofitInstance
import com.lucas.weatherapi.ui.composable.WeatherCard
import com.lucas.weatherapi.ui.composable.getDay
import com.lucas.weatherapi.ui.theme.colorSuggestion
import com.lucas.weatherapi.ui.viewModel.WeatherViewModel
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {

    val context = LocalContext.current
    // Valores para obtener los datos del viewModel al Model
    val forecastData = viewModel.forecastData.observeAsState().value
    val currentData = viewModel.currentData.observeAsState().value
    // Obtiene los datos de weatherData
    val forecastList = forecastData?.data ?: emptyList()
    val currentList = currentData?.data ?: emptyList()

    // Card seleccionado
    var selectedCardIndex by remember { mutableIntStateOf(0) }
    val selectedForecastDay = forecastList.getOrNull(selectedCardIndex)

    // Logica del Search (iniciar buscador o iniciarlo vacio)
    var isSearchActive by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    // Lenguaje actual del dispositivo
    val languageCode = Locale.getDefault().language

    // Acceso a las ciudades del csv
    LaunchedEffect(Unit) {
        viewModel.loadCities(context)
    }

    // Sugerencias de ciudades
    val suggestions by viewModel.searchCities.observeAsState()

    // Carga inicial
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        isLoading = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        TextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Burriana, ES", color = Color.LightGray) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    } else {
                        Text(text = "WeatherBit", color = Color.White, fontSize = 25.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Blue),
                actions = {
                    if (isSearchActive) {
                        IconButton(onClick = {
                            viewModel.searchCities(searchText.text)
                        }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = {
                            searchText = TextFieldValue("")
                            isSearchActive = false
                            viewModel.clearSuggestions()
                        }) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Cancelar",
                                tint = Color.White
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            isSearchActive = true
                        }) {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = "Buscar",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { (context as Activity).finish() }) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Salir",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }, content = { paddingValues ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (isLoading) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .verticalScroll(rememberScrollState())
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "${forecastData?.city_name}, ${forecastData?.country_code}",
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 25.dp, top = 20.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = getDay(selectedForecastDay?.valid_date),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 25.dp, top = 20.dp)
                                )
                                Text(
                                    text = "${selectedForecastDay?.valid_date}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 25.dp)
                                )
                            }
                        }

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            itemsIndexed(forecastList) { index, forecastDay ->
                                WeatherCard(
                                    forecastday = forecastDay,
                                    currentDay = currentList.firstOrNull(),
                                    onClick = { selectedCardIndex = index }

                                )
                            }
                        }
                    }

                    if (suggestions?.isNotEmpty() == true && isSearchActive) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.TopCenter,
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(400.dp)
                                    .padding(top = 20.dp, bottom = 20.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color.Black,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .background(Color.White)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .verticalScroll(rememberScrollState())
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "Resultados: ${suggestions?.size}",
                                        color = Color.Black,
                                        modifier = Modifier
                                            .align(Alignment.End)
                                            .padding(bottom = 8.dp)
                                    )

                                    suggestions?.forEach { (city, country) ->
                                        Text(
                                            text = "$city, $country",
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp, horizontal = 8.dp)
                                                .background(
                                                    color = colorSuggestion,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .clickable {
                                                    val cityWithCountry = "$city,$country"
                                                    searchText = TextFieldValue(city)
                                                    viewModel.getWeatherForecast(
                                                        cityWithCountry,
                                                        languageCode,
                                                        RetrofitInstance.days,
                                                        RetrofitInstance.API_KEY
                                                    )
                                                    viewModel.getWeatherCurrent(
                                                        cityWithCountry,
                                                        languageCode,
                                                        RetrofitInstance.API_KEY
                                                    )
                                                    viewModel.clearSuggestions()
                                                    isSearchActive = false
                                                }
                                                .padding(12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Text(
                        text = "Ultima actualizacion: " + currentList.firstOrNull()?.ob_time.toString(),
                        fontSize = 10.sp,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 20.dp, bottom = 10.dp)
                            .align(Alignment.BottomEnd),
                        color = Color.Red
                    )
                } else {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    )
}


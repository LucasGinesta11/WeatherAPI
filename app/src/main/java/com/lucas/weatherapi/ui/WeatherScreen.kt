package com.lucas.weatherapi.ui

import android.app.Activity
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import com.lucas.weatherapi.viewModel.WeatherViewModel
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

    val languageCode = Locale.getDefault().language

    // Acceso a las ciudades del csv
    LaunchedEffect(Unit) {
        viewModel.loadCities(context)
    }

    val suggestions by viewModel.searchCities.observeAsState()
    val citiesLoaded = viewModel.citiesLoaded.observeAsState(false).value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        TextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                if (it.text.isNotEmpty() && citiesLoaded) {
                                    viewModel.searchCities(it.text)
                                }

                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Buscar...", color = Color.White) },
                            singleLine = true,
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
                            viewModel.getWeatherForecast(
                                searchText.text,
                                languageCode,
                                8,
                                "3cd8b92528154d97ac76b917d315cf81"
                            )
                            viewModel.getWeatherCurrent(
                                searchText.text,
                                languageCode,
                                "3cd8b92528154d97ac76b917d315cf81"
                            )
                            isSearchActive = false
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
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            modifier = Modifier
                                .width(800.dp)
                                .padding(top = 10.dp)
                                .background(Color(0xFFD6E6FB))
                        ) {
                            suggestions?.forEach { (city, country) ->
                                Text(
                                    text = "$city, $country",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                        .background(Color.White)
                                        .clickable {
                                            val cityWithCountry = "$city,$country"
                                            searchText = TextFieldValue(city)
                                            viewModel.getWeatherForecast(
                                                cityWithCountry,
                                                languageCode,
                                                8,
                                                "3cd8b92528154d97ac76b917d315cf81"
                                            )
                                            viewModel.getWeatherCurrent(
                                                cityWithCountry,
                                                languageCode,
                                                "3cd8b92528154d97ac76b917d315cf81"
                                            )
                                            isSearchActive = false
                                        }
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "Ultima actualizacion: " + currentList.firstOrNull()?.ob_time.toString(),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomEnd),
                    color = Color.Red
                )
            }
        }
    )
}


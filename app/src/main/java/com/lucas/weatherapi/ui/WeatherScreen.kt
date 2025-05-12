package com.lucas.weatherapi.ui

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Tab
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRow
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lucas.weatherapi.viewModel.WeatherViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {

    val context = LocalContext.current
    val weatherData = viewModel.weatherData.observeAsState().value
    val forecastList = weatherData?.forecast?.forecastday ?: emptyList()


    var isSearchActive by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    var temperature by remember { mutableStateOf(true) }
    var selectedCardIndex by remember { mutableIntStateOf(0) } // Ninguno seleccionado inicialmente
    val selectedForecastDay = forecastList.getOrNull(selectedCardIndex)
    val forecastDate = selectedForecastDay?.date?.let { LocalDate.parse(it) }

    val today = LocalDate.now()
    val isToday = forecastDate == today

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
                        Text(text = "WeatherAPI", color = Color.White, fontSize = 25.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Blue),
                actions = {
                    if (isSearchActive) {
                        IconButton(onClick = {
                            viewModel.getWeatherForecast(
                                "077486a2e1f44c3185c72052250905",
                                searchText.text
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
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {

                Text(
                    text = "${weatherData?.location?.name}",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(20.dp)
                )

                Spacer(modifier = Modifier.padding(15.dp))

                Row(Modifier.fillMaxWidth()) {

                    AsyncImage(
                        model = "https:${weatherData?.current?.condition?.icon}",
                        contentDescription = "Imagen de tiempo diario",
                        modifier = Modifier
                            .width(90.dp)
                            .height(40.dp)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = when {
                                // Si es hoy mostrar temperatura actual
                                isToday -> {
                                    if (temperature) {
                                        "${weatherData?.current?.temp_c}"
                                    } else {
                                        "${weatherData?.current?.temp_f}"
                                    }
                                }
                                // Si no, mostrar la maxima del dia seleccionado
                                else -> {
                                    if (temperature) {
                                        "${selectedForecastDay?.day?.maxtemp_c}"
                                    } else {
                                        "${selectedForecastDay?.day?.maxtemp_f}"
                                    }
                                }
                            },
                            fontSize = 30.sp
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        TextButton(
                            onClick = { temperature = true },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.sizeIn(minWidth = 10.dp, minHeight = 10.dp)
                        ) {
                            Text(text = "ºC", fontSize = 20.sp)
                        }

                        Text(
                            text = "|",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                        TextButton(
                            onClick = { temperature = false },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.sizeIn(minWidth = 10.dp, minHeight = 10.dp)
                        ) {
                            Text(text = "ºF", fontSize = 20.sp)
                        }
                    }

                    Spacer(modifier = Modifier.padding(30.dp))

                    Column(Modifier.padding(start = 20.dp)) {
                        Text("Precipitation: ${selectedForecastDay?.day?.daily_chance_of_rain}%")
                        Text(
                            if (isToday) {
                                "Humidity: ${weatherData?.current?.humidity}%"
                            } else {
                                "Humidity: ${selectedForecastDay?.day?.avghumidity}%"
                            }
                        )
                        Text(if(isToday){
                            "Wind: ${weatherData?.current?.wind_kph} km/h"
                        }else{
                            "Wind: ${selectedForecastDay?.day?.maxwind_kph} km/h"
                        })
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Column(modifier = Modifier.padding(end = 20.dp)) {
                        Text("Weather", fontSize = 30.sp)

                        Text(
                            text =
                                if (isToday) {
                                    "${weatherData?.current?.last_updated}"
                                } else {
                                    obtenerDia(weatherData?.forecast?.date)
                                }
                        )
                        Text("${selectedForecastDay?.day?.condition?.text}")
                    }
                }

                Spacer(modifier = Modifier.padding(50.dp))

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    itemsIndexed(forecastList) { index, forecastDay ->
                        WeatherCard(
                            forecastday = forecastDay,
                            temperature = temperature,
                            isSelected = index == selectedCardIndex,
                            onClick = { selectedCardIndex = index }
                        )
                    }

                }
            }
        }
    )
}

@Composable
fun SimpleTabs() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf("Temperature", "Precipitaciones", "Viento")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

//        when (selectedTabIndex){
//            0 -> Text("Temperatura", modifier = Modifier.padding(16.dp))
//            1 ->
//            2 ->
//        }
    }
}

fun obtenerDia(fecha: String?): String {
    return try {
        if (fecha.isNullOrBlank()) return "Sin fecha"
        val date = LocalDate.parse(fecha)
        date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("en", "GB"))
    } catch (e: Exception) {
        "Fecha inválida"
        e.printStackTrace()
    }.toString()
}
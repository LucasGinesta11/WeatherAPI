package com.lucas.weatherapi.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lucas.weatherapi.viewModel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {

    val context = LocalContext.current
    val weatherData = viewModel.weatherData.observeAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "WeatherAPI", color = Color.White, fontSize = 25.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Blue),
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Buscar ciudad",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { (context as Activity).finish() }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Salir de la aplicacion",
                            tint = Color.White
                        )
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

                    Text(text = "Ciudad: ${weatherData?.location?.name}", fontSize = 25.sp)
                    Text(text = "Temperatura: ${weatherData?.current?.temp_c}", fontSize = 20.sp)
                    Text(text = "Local Time: ${weatherData?.location?.localTime}", fontSize = 20.sp)


                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                    TextButton(onClick = {}) {
                        Text(text = "20", fontSize = 24.sp)
                    }

                    Column(Modifier.padding(start = 20.dp)) {
                        Text("precic")
                        Text("Humedad")
                        Text("Viento")
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Tiempo")
                        Text("dia")
                        Text("nublad")
                    }
                }

                SimpleTabs()

                Spacer(modifier = Modifier.padding(20.dp))

                WeatherCard(viewModel)
            }

        }
    )
}

@Composable
fun SimpleTabs() {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Temperatura", "Precipitaciones", "Viento")

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
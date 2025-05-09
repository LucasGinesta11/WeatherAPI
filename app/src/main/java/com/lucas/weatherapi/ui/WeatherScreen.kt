package com.lucas.weatherapi.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
                weatherData?.let { data ->
                    Text(text = "Ciudad: ${data.location.name}", fontSize = 25.sp)
                    Text(text = "Temperatura: ${data.current.temp_c}", fontSize = 20.sp)
                    Text(text = "Local Time: ${data.location.localTime}", fontSize = 20.sp)
                }

                Text("Hoooola")
            }

        }
    )
}
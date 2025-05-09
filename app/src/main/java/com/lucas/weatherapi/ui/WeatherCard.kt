package com.lucas.weatherapi.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lucas.weatherapi.viewModel.WeatherViewModel

@Composable
fun WeatherCard(viewModel: WeatherViewModel){

    val weatherData = viewModel.weatherData.observeAsState().value
    Card (onClick = {}, modifier = Modifier.height(150.dp).width(120.dp)){
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${weatherData?.forecast?.forecast_day?.get(0)?.date}")

            Spacer(modifier = Modifier.padding(8.dp))

//            Image()
            Text("Imagen")

            Spacer(modifier = Modifier.padding(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("${weatherData?.forecast?.forecast_day?.get(0)?.day?.maxtemp_c}")
                Text("${weatherData?.forecast?.forecast_day?.get(0)?.day?.mintemp_c}")
            }
        }
    }
}

//@Composable
//@Preview
//fun Preview(viewModel: WeatherViewModel){
//    WeatherCard(viewModel)
//}
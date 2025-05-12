package com.lucas.weatherapi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lucas.weatherapi.data.model.ForecastDay

@Composable
fun WeatherCard(
    forecastday: ForecastDay,
    temperature: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cardColor = if (isSelected) Color.LightGray else Color.White

    Card(
        onClick = onClick,
        modifier = Modifier
            .height(130.dp)
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(obtenerDia(forecastday.date))

            Spacer(modifier = Modifier.padding(8.dp))

            AsyncImage(
                model = "https:${forecastday.day.condition.icon}",
                contentDescription = "Icono del clima",
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    if (temperature) {
                        "${forecastday.day.maxtemp_c}º"
                    } else {
                        "${forecastday.day.maxtemp_f}º"
                    }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Text(
                    if (temperature) {
                        "${forecastday.day.mintemp_c}º"
                    } else {
                        "${forecastday.day.mintemp_f}º"
                    }
                )
            }
        }
    }
}

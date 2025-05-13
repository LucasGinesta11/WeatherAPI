package com.lucas.weatherapi.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lucas.weatherapi.R
import com.lucas.weatherapi.data.model.ForecastDay
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeatherCard(forecastday: ForecastDay) {
    Card(
        onClick = {},
        modifier = Modifier
            .height(370.dp)
            .width(170.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(getDay(forecastday.valid_date), fontSize = 20.sp)

            Spacer(modifier = Modifier.padding(8.dp))

            AsyncImage(
                model = "https://www.weatherbit.io/static/img/icons/${forecastday.weather.icon}.png",
                contentDescription = "Icono del clima",
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                forecastday.weather.description,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("${forecastday.max_temp}º", color = Color.Red)

                Spacer(modifier = Modifier.padding(10.dp))

                Text("${forecastday.min_temp}º", color = Color.Blue)
            }

            Spacer(Modifier.padding(top = 10.dp))

            Column {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.prec),
                        contentDescription = "",
                        Modifier.width(25.dp).height(25.dp)
                    )
                    Text("${forecastday.pop}%")
                }
                Text("Humidity: ${forecastday.rh}%", modifier = Modifier.padding(5.dp))
                Text("Wind: ${forecastday.wind_spd} m/s", modifier = Modifier.padding(5.dp))
                Text("Clouds: ${forecastday.clouds}%", modifier = Modifier.padding(5.dp))
                Text(
                    "Sunrise: ${convertUnixToLocalTime(forecastday.sunrise_ts, "Europe/Madrid")}",
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    "Sunset: ${convertUnixToLocalTime(forecastday.sunset_ts, "Europe/Madrid")}",
                    modifier = Modifier.padding(5.dp)
                )

            }
        }
    }
}

// Metodo para pasar los valores de timestamps en segundos a hora local
fun convertUnixToLocalTime(timestamp: Long?, zoneId: String): String {
    return timestamp?.let {
        val instant = Instant.ofEpochSecond(it)
        val localTime = instant.atZone(ZoneId.of(zoneId)).toLocalTime()
        localTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    } ?: "--:--"
}

// Obtiene el dia de la semana a partir de su fecha
fun getDay(fecha: String?): String {
    return try {
        val date = LocalDate.parse(fecha)
        date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("en", "GB"))
    } catch (e: Exception) {
        "Fecha inválida"
        e.printStackTrace()
    }.toString()
}

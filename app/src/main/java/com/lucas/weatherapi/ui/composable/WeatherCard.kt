package com.lucas.weatherapi.ui.composable

import android.annotation.SuppressLint
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lucas.weatherapi.R
import com.lucas.weatherapi.data.model.CurrentDay
import com.lucas.weatherapi.data.model.ForecastDay
import com.lucas.weatherapi.ui.theme.colorCard
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun WeatherCard(forecastday: ForecastDay, currentDay: CurrentDay?, onClick: () -> Unit) {

    val isToday = LocalDate.parse(forecastday.valid_date) == LocalDate.now()

    Card(
        onClick = onClick,
        modifier = Modifier
            .height(370.dp)
            .width(240.dp)
            .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = colorCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(getDay(forecastday.valid_date), fontSize = 20.sp)

            Spacer(modifier = Modifier.padding(4.dp))

            if (isToday) {
                Text("${currentDay?.temp}º", fontSize = 20.sp)
            } else {
                // Espacio para compensar la temp actual
                Spacer(Modifier.padding(12.dp))
            }

            Spacer(modifier = Modifier.padding(8.dp))

            AsyncImage(
                model = "https://www.weatherbit.io/static/img/icons/${forecastday.weather.icon}.png",
                contentDescription = "Icono del clima",
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
            )

            Spacer(modifier = Modifier.padding(12.dp))

            Text(
                forecastday.weather.description,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("${forecastday.max_temp}º", color = Color.Red, fontSize = 15.sp)

                Spacer(modifier = Modifier.padding(10.dp))

                Text("${forecastday.min_temp}º", color = Color.Blue, fontSize = 15.sp)
            }

            Spacer(Modifier.padding(top = 15.dp))

            Row {
                Column(Modifier.padding(start = 4.dp)) {

                    WeatherVariable(
                        R.drawable.prec,
                        "Probabilidad de lluvia",
                        "${forecastday.pop}%"
                    )

                    WeatherVariable(
                        R.drawable.hum,
                        "Cantidad de humedad",
                        "${forecastday.rh}%"
                    )

                    WeatherVariable(
                        R.drawable.total,
                        "Cantidad de lluvia",
                        String.format("%.2f mm", forecastday.precip)
                    )

                    WeatherVariable(
                        R.drawable.sunrise,
                        "Amanecer",
                        convertUnixToLocalTime(forecastday.sunrise_ts, "Europe/Madrid")
                    )
                }

                Column(Modifier.padding(start = 8.dp)) {

                    WeatherVariable(
                        R.drawable.vien,
                        "Velocidad del viento",
                        "${forecastday.wind_spd} m/s"
                    )

                    WeatherVariable(
                        R.drawable.grados,
                        "Direccion del viento",
                        "${forecastday.wind_dir}º, ${forecastday.wind_cdir}"
                    )

                    WeatherVariable(
                        R.drawable.nieve,
                        "Cantidad de nieve",
                        String.format("%.2f mm", forecastday.snow)
                    )

                    WeatherVariable(
                        R.drawable.sunset,
                        "Atardecer",
                        convertUnixToLocalTime(forecastday.sunset_ts, "Europe/Madrid")
                    )
                }
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
        date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
    } catch (e: Exception) {
        e.printStackTrace()
    }.toString()
}

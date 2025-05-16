package com.lucas.weatherapi.data.model

data class CurrentResponse(
    val data: List<CurrentDay>
)

data class CurrentDay(
    val ob_time: String,
    val temp: Float
)
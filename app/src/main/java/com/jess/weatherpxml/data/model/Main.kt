package com.jess.weatherpxml.data.model

import com.google.gson.annotations.SerializedName

data class Main(
   @SerializedName("feels_like") val feels_like: Double,
   @SerializedName("humidity")  val humidity: Int,
   @SerializedName("pressure")  val pressure: Int,
   @SerializedName("temp")  val temp: Double,
)
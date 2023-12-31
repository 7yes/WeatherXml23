package com.jess.weatherpxml.data.model

import com.google.gson.annotations.SerializedName

data class WeatherModel(
   @SerializedName("base") val base: String,
   @SerializedName("clouds")val clouds: Clouds,
   @SerializedName("cod") val cod: Int,
   @SerializedName("coord") val coord: Coord,
   @SerializedName("main") val main: Main,
   @SerializedName("name") val name: String,
   @SerializedName("sys") val sys: Sys,
   @SerializedName("visibility") val visibility: Int,
   @SerializedName("weather") val weather: List<Weather>,
   @SerializedName("wind") val wind: Wind
)
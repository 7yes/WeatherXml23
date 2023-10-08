package com.jess.weatherpxml.domain.model

import com.jess.weatherpxml.data.model.Weather
import com.jess.weatherpxml.data.model.WeatherModel

data class WeatherInfo(
    val clouds: Int,//
    val feelsLike: Int,//
    val humidity: Int,
    val pressure: Int,
    val temp: Int,//
    val sunrise: Int,
    val sunset: Int,
    val visibility: Int,
    val weather: List<Weather>,//
    val windSpeed:Double,//
    val city:String,
    val country:String
)

fun WeatherModel.toDomain() = WeatherInfo(clouds = clouds.all, feelsLike =((main.feels_like*9/5)/10+32).toInt(), humidity = main.humidity, pressure = main.pressure, temp = ((main.temp*9/5)/10+32).toInt(), sunrise = sys.sunrise, sunset = sys.sunset, visibility=visibility, weather = weather, windSpeed = wind.speed, city = name, country = sys.country )




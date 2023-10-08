package com.jess.weatherpxml.domain.model

import com.jess.weatherpxml.data.model.Weather
import com.jess.weatherpxml.data.model.WeatherModel

data class WeatherInfo(
    val clouds: Int,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val sunrise: Int,
    val sunset: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val windSpeed:Double,
    val city:String
)

fun WeatherModel.toDomain() = WeatherInfo(clouds = clouds.all, feelsLike = main.feels_like, humidity = main.humidity, pressure = main.pressure, temp = main.temp, sunrise = sys.sunrise, sunset = sys.sunset, visibility=visibility, weather = weather, windSpeed = wind.speed, city = name)




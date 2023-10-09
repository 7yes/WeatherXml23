package com.jess.weatherpxml.data.model

import com.jess.weatherpxml.data.WeatherApi
import javax.inject.Inject

class WeatherService @Inject constructor(private val weatherApi: WeatherApi){
    suspend fun getCityWeather(city:String): WeatherModel? {
        return weatherApi.getCityForecast(city = city).body()
    }
}
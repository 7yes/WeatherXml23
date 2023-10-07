package com.jess.weatherpxml.domain

import com.jess.weatherpxml.data.model.WeatherModel

interface WeatherRepo {
    suspend fun getCityWeather(city:String): WeatherModel?
}
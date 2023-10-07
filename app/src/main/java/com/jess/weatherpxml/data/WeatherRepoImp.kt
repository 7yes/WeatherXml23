package com.jess.weatherpxml.data

import com.jess.weatherpxml.data.model.WeatherModel
import com.jess.weatherpxml.data.model.WeatherService
import com.jess.weatherpxml.domain.WeatherRepo

class WeatherRepoImp(private val weatherService: WeatherService):WeatherRepo {
    override suspend fun getCityWeather(city: String): WeatherModel? {

       return weatherService.getCityWeather(city)
    }
}
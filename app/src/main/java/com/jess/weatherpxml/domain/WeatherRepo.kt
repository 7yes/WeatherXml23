package com.jess.weatherpxml.domain

import com.jess.weatherpxml.domain.model.EndPointType
import com.jess.weatherpxml.domain.model.WeatherInfo
import com.jess.weatherpxml.domain.utils.Resource

interface WeatherRepo {
    suspend fun getCityWeather(callType: EndPointType): Resource<WeatherInfo>
}
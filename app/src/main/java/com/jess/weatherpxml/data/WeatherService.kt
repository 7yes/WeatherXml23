package com.jess.weatherpxml.data

import com.jess.weatherpxml.data.model.WeatherModel
import com.jess.weatherpxml.domain.model.EndPointType
import javax.inject.Inject

class WeatherService @Inject constructor(private val weatherApi: WeatherApi){
    suspend fun getCityWeather(callType:EndPointType): WeatherModel? {
        return when(callType){
            is EndPointType.WITH_CITY -> weatherApi.getCityForecast(city= callType.city).body()
            is EndPointType.WITH_LOCATION -> weatherApi.getLatLonForecast(lon = callType.lon, lat = callType.lat).body()
        }
    }
}
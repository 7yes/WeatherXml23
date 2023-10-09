package com.jess.weatherpxml.data

import com.jess.weatherpxml.domain.WeatherRepo
import com.jess.weatherpxml.domain.model.EndPointType
import com.jess.weatherpxml.domain.model.WeatherInfo
import com.jess.weatherpxml.domain.model.toDomain
import com.jess.weatherpxml.domain.utils.Resource

class WeatherRepoImp(private val weatherService: WeatherService) : WeatherRepo {
    lateinit var data: WeatherInfo

    //With more time  we could implement room DB so if the Api is no t responding we can get data from room
    override suspend fun getCityWeather(callType: EndPointType): Resource<WeatherInfo> {
       return try {
        Resource.Success(data= weatherService.getCityWeather(callType)?.toDomain() )
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Error(message = e.message?:"An unknown error occurred")
        }
    }
}
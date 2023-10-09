package com.jess.weatherpxml.domain.usecases

import com.jess.weatherpxml.domain.WeatherRepo
import com.jess.weatherpxml.domain.model.EndPointType
import com.jess.weatherpxml.domain.model.WeatherInfo
import com.jess.weatherpxml.domain.utils.Resource
import javax.inject.Inject

// in the Use case we put the business logic, in the back for example
// if the enduser has a daily limit of $100 per day we use that logic here
class GetWeatherUseCase @Inject constructor(private val repository: WeatherRepo) {
    suspend operator fun invoke(callType: EndPointType): Resource<WeatherInfo> {
        val data = repository.getCityWeather(callType)
        // here we play with the business logic
        return data
    }
}
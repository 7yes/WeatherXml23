package com.jess.weatherpxml.ui

import com.jess.weatherpxml.domain.model.WeatherInfo
sealed class ResultState {
    object LOADING : ResultState()
    class SUCCESS(val results: WeatherInfo) : ResultState()
    class ERROR(val error: Throwable) : ResultState()
    class ERROR_CONECTION(val error: String) : ResultState()
}

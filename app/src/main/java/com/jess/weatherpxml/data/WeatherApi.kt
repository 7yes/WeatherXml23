package com.jess.weatherpxml.data

import com.jess.weatherpxml.data.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET(PATH)
    suspend fun getCityForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String = API_KEY

    ): Response<WeatherModel>

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
        const val PATH = "data/2.5/weather"
        const val API_KEY = "8933ecd5eed0f2ebf8b423951c2fdc20"
    }
}

//https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=8933ecd5eed0f2ebf8b423951c2fdc20
//https://api.openweathermap.org/data/2.5/weather?q=Miami&appid=8933ecd5eed0f2ebf8b423951c2fdc20
//https://api.openweathermap.org/data/2.5/weather?q=Miami,us&appid=8933ecd5eed0f2ebf8b423951c2fdc20
package com.jess.weatherpxml.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jess.weatherpxml.data.model.WeatherModel
import com.jess.weatherpxml.domain.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: WeatherRepo) : ViewModel() {

    private val _cityWeather  = MutableLiveData<WeatherModel>()
    val cityWeather: LiveData<WeatherModel> = _cityWeather

    fun getCityWeather(city:String){
        viewModelScope.launch(Dispatchers.IO) {
            val body = repo.getCityWeather(city)

            body.let {
                println("vamos ${it?.name} main temp ${it?.main?.temp}  main ${it?.main}  weather${it?.weather} cod ${it?.cod} base:${it?.base} clouds:${it?.clouds}  sys:${it?.sys} tiemzone:${it?.timezone} visibility:${it?.visibility} wind:${it?.wind}")
            }
        }
    }
}
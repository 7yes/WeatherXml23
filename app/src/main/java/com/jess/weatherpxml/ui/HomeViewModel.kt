package com.jess.weatherpxml.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jess.weatherpxml.core.isNull
import com.jess.weatherpxml.domain.model.EndPointType
import com.jess.weatherpxml.domain.model.WeatherInfo
import com.jess.weatherpxml.domain.usecases.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCaseCity: GetWeatherUseCase) : ViewModel() {

    private val _state = MutableLiveData<ResultState>()
    val state: LiveData<ResultState> = _state

    private val _shouldOpenHome = MutableLiveData<Boolean>(true)
    val shouldOpenHome: LiveData<Boolean> = _shouldOpenHome

    private val _result = MutableLiveData<WeatherInfo>()
    val result: LiveData<WeatherInfo> = _result

    fun getCityWeather(city: String) {
        _state.value = ResultState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = useCaseCity(EndPointType.WITH_CITY(city)).data
                withContext(Dispatchers.Main) {
                    if (data.isNull()) {
                        _state.value =
                            ResultState.ERROR_CONECTION("City dosen't exits or connection is lost")
                    } else {
                        _state.value = data?.let { ResultState.SUCCESS(it) }
                    }
                }
            } catch (e: Exception) {
                _state.value = ResultState.ERROR(e)
            }
        }
    }

    fun updateNavigationStatus(nav:Boolean) {
        _shouldOpenHome.value = nav
    }
    fun updateCityData(results: WeatherInfo) {
        _result.value = results
    }
}
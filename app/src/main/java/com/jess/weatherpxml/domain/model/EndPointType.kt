package com.jess.weatherpxml.domain.model

sealed class EndPointType{
    class WITH_CITY(val city:String):EndPointType()
    class WITH_LOCATION(val lat:Double,val lon:Double):EndPointType()
}

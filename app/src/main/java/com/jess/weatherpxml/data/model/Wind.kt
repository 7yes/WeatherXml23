package com.jess.weatherpxml.data.model

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed") val speed: Double
)
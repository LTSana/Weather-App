package com.example.wheatherapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherList>?,
    val city: WeatherCity
)

@Serializable
data class WeatherList (
    val dt: Int,
    val visibility: Int,
    val pop: Double,
    val dt_txt: String,
    val main: WeatherListMain,
    val weather: List<WeatherListWeather>,
    val clouds: WeatherListClouds,
    val wind: WeatherListWind,
    val rain: WeatherListRain? = null,
    val snow: WeatherListSnow? = null,
    val sys: WeatherListSys?
)

@Serializable
data class WeatherListMain (
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val sea_level: Int,
    val grnd_level: Int,
    val humidity: Int,
    val temp_kf: Double
        )

@Serializable
data class WeatherListWeather (
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
        )

@Serializable
data class WeatherListClouds (
    val all: Int
        )

@Serializable
data class WeatherListWind (
    val speed: Double,
    val deg: Int,
    val gust: Double
        )

@Serializable
data class WeatherListRain (
    @SerialName("3h")
    val threeHours: Double?
        )

@Serializable
data class WeatherListSnow (
    @SerialName("3h")
    val threeHours: Double?
        )

@Serializable
data class WeatherListSys (
    val pod: String
        )

@Serializable
data class WeatherCity (
    val id: Int,
    val name: String,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Int,
    val sunset: Int,
    val coord: WeatherCityCoord
)

@Serializable
data class WeatherCityCoord (
    val lat: Double,
    val lon: Double
)

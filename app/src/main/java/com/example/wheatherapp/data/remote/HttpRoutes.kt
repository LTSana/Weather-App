package com.example.wheatherapp.data.remote

object HttpRoutes {

    private const val API_KEY = "ADD_KEY_HERE"
    private const val BASE_URL_2 = "https://api.openweathermap.org"
    const val WEATHER = "$BASE_URL_2/data/2.5/forecast?appid=$API_KEY&units=metric"
}
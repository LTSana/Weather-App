package com.example.wheatherapp.data.remote

import com.example.wheatherapp.data.remote.dto.WeatherResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*

class PostServiceImpl(
    private val client: HttpClient
): PostService {

    override suspend fun getWeather(lat: Double, lon: Double): WeatherResponse? {
        return try {
            client.get {
                url(HttpRoutes.WEATHER)
                parameter("lat", lat)
                parameter("lon", lon)
            }
        } catch (e: RedirectResponseException) {
            // 3xx - Responses
            println("Error: ${e.response.status.description}")
            null
        } catch (e: ClientRequestException) {
            // 4xx - Responses
            println("Error: ${e.response.status.description}")
            null
        } catch (e: ServerResponseException) {
            // 5xx - Responses
            println("Error: ${e.response.status.description}")
            null
        } catch (e: Exception) {
            // General exception, maybe no internet
            println("Error: ${e.message}")
            null
        }
    }
}
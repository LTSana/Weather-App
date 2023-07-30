package com.example.wheatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.wheatherapp.data.remote.PostService
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    private val requestcode: Int = 123456789
    private val service = PostService.create()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the elements from the UI
        var dateUI: TextView = findViewById(R.id.date)
        var timeUI: TextView = findViewById(R.id.timestamp)
        var locationUI: TextView = findViewById(R.id.location)
        var weatherIconUI: ImageView = findViewById(R.id.weatherIcon)
        var temperatureUI: TextView = findViewById(R.id.digress)

        // Create our coroutine
        val coroutine = CoroutineScope(Dispatchers.Default)

        // Remove the Title Bar basically
        supportActionBar?.hide()

        // Get the date and time
        val formatted = DateTimeFormatter.ofPattern("EE, dd MMM")
        val date = LocalDateTime.now().format(formatted)
        // Add the date to the screen
        dateUI.text = date

        // We get the time on UI load
        timeUI.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))

        // Check if the user has given us the permission to access the device location
        if (hasLocationPermissions()) {

            // Get the device location manager
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // Get the providers Network and GPS
            val hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            var locationByGps: Location? = null
            var locationByNetwork: Location? = null
            var currentLocation: Location
            var latitude: Double = 0.0
            var longitude: Double = 0.0
            var locationName: String? = null

            val gpsLocationListener: LocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationByGps= location
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }

            if (hasGPS) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    gpsLocationListener
                )

                val lastKnownLocationByGps =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                lastKnownLocationByGps?.let {
                    currentLocation = lastKnownLocationByGps
                    latitude = currentLocation.latitude
                    longitude = currentLocation.longitude
                }
            }

            coroutine.launch {
                val items = service.getWeather(latitude, longitude)
                locationUI.text = items?.city?.name
                var currentTemp = items?.list?.get(0)?.main?.temp?.toInt()?.toString()
                temperatureUI.text = "${currentTemp}°C"
                weatherIconUI.setImageResource(when (items?.list?.get(0)?.weather?.get(0)?.icon) {
                    "01d", "01n" -> R.drawable.sunny_icon
                    "02d", "02n" -> R.drawable.sunny_cloudy_icon
                    "09d", "09n" -> R.drawable.ranny_icon
                    else -> R.drawable.sunny_icon
                })
            }
        } else {
            println("We do not have permission to access device location.")
        }
    }

    private fun hasLocationPermissions(): Boolean {
        /* Check if the application has permission to access device location. */

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        // Request permission to access the device location
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            requestcode
        )
        return false
    }
}
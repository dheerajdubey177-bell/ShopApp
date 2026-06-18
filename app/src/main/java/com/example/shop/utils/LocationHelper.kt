package com.example.shop.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

object LocationHelper {

    // Simple coordinates for demonstration
    private val cityCoordinates = mapOf(
        "Bhilai" to Pair(21.1938, 81.3509),
        "Raipur" to Pair(21.2514, 81.6296),
        "Pune" to Pair(18.5204, 73.8567),
        "Mumbai" to Pair(19.0760, 72.8777),
        "Delhi" to Pair(28.6139, 77.2090)
    )

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context, onResult: (String?) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val nearestCity = findNearestCity(location.latitude, location.longitude)
                onResult(nearestCity)
            } else {
                onResult(null)
            }
        }.addOnFailureListener {
            onResult(null)
        }
    }

    private fun findNearestCity(lat: Double, lon: Double): String? {
        var minDistance = Float.MAX_VALUE
        var nearestCity: String? = null

        cityCoordinates.forEach { (city, coords) ->
            val results = FloatArray(1)
            Location.distanceBetween(lat, lon, coords.first, coords.second, results)
            if (results[0] < minDistance) {
                minDistance = results[0]
                nearestCity = city
            }
        }

        // Only suggest if within 50km
        return if (minDistance < 50000) nearestCity else null
    }
}

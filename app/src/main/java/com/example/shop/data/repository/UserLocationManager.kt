package com.example.shop.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserLocationManager {
    private val _currentLocation = MutableStateFlow("")
    val currentLocation: StateFlow<String> = _currentLocation

    fun updateLocation(location: String) {
        _currentLocation.value = location
    }
}

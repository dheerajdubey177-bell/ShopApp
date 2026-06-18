package com.example.shop.data.repository

import androidx.compose.runtime.mutableStateOf

object SelectedRestaurantManager {
    val selectedRestaurantId = mutableStateOf<Int?>(null)
    val selectedRestaurantName = mutableStateOf("")
}

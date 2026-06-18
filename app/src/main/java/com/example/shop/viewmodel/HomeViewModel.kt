package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.data.repository.RestaurantRepository
import com.example.shop.data.local.entity.RestaurantEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    private val _restaurants = MutableStateFlow<List<RestaurantEntity>>(emptyList())
    val restaurants: StateFlow<List<RestaurantEntity>> = _restaurants

    init {
        loadRestaurants()
    }

    fun loadRestaurants() {
        viewModelScope.launch {
            _restaurants.value = restaurantRepository.getAllRestaurants()
        }
    }
}

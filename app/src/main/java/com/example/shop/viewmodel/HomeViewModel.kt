package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.data.local.SessionManager
import com.example.shop.data.repository.RestaurantRepository
import com.example.shop.data.repository.UserLocationManager
import com.example.shop.data.local.entity.RestaurantEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val restaurantRepository: RestaurantRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _restaurants = MutableStateFlow<List<RestaurantEntity>>(emptyList())
    
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    val userLocation: StateFlow<String> = UserLocationManager.currentLocation

    private val _filteredRestaurantsState = MutableStateFlow<List<RestaurantEntity>>(emptyList())
    val filteredRestaurantsState: StateFlow<List<RestaurantEntity>> = _filteredRestaurantsState

    init {
        // Initial sync from session to global manager
        val savedLoc = sessionManager.getLocation() ?: ""
        if (savedLoc.isNotBlank() && UserLocationManager.currentLocation.value.isBlank()) {
            UserLocationManager.updateLocation(savedLoc)
        }

        loadRestaurants()
        
        // React to global location changes
        viewModelScope.launch {
            UserLocationManager.currentLocation.collectLatest {
                updateFilteredList()
            }
        }
    }

    fun loadRestaurants() {
        viewModelScope.launch {
            _restaurants.value = restaurantRepository.getAllRestaurants()
            updateFilteredList()
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        updateFilteredList()
    }

    fun onCategorySelect(category: String?) {
        if (_selectedCategory.value == category) {
            _selectedCategory.value = null
        } else {
            _selectedCategory.value = category
        }
        updateFilteredList()
    }

    fun onLocationChange(location: String) {
        sessionManager.saveLocation(location)
        UserLocationManager.updateLocation(location)
        // updateFilteredList is called by the collector in init
    }

    private fun updateFilteredList() {
        val restaurants = _restaurants.value
        val text = _searchText.value
        val category = _selectedCategory.value
        val location = UserLocationManager.currentLocation.value

        _filteredRestaurantsState.value = restaurants.filter { rest ->
            val matchesLocation = location.isBlank() || rest.region.equals(location, ignoreCase = true)
            val matchesSearch = text.isBlank() || rest.name.contains(text, ignoreCase = true) || rest.description.contains(text, ignoreCase = true)
            val matchesCategory = category == null || rest.cuisineType.contains(category, ignoreCase = true)
            
            matchesLocation && matchesSearch && matchesCategory
        }
    }
}

package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.data.repository.FoodRepository
import com.example.shop.data.repository.RestaurantRepository
import com.example.shop.data.repository.MenuRepository
import com.example.shop.data.local.entity.FoodEntity
import com.example.shop.data.local.entity.RestaurantEntity
import com.example.shop.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MenuViewModel(
    private val foodRepository: FoodRepository,
    private val restaurantRepository: RestaurantRepository,
    private val menuRepository: MenuRepository
) : ViewModel() {

    private val _restaurant = MutableStateFlow<RestaurantEntity?>(null)
    val restaurant: StateFlow<RestaurantEntity?> = _restaurant

    private val _foods = MutableStateFlow<List<FoodEntity>>(emptyList())
    val foods: StateFlow<List<FoodEntity>> = _foods

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories

    fun loadMenu(restaurantId: Int) {
        viewModelScope.launch {
            _restaurant.value = restaurantRepository.getRestaurantById(restaurantId)
            _foods.value = foodRepository.getFoodsByRestaurant(restaurantId)
            _categories.value = menuRepository.getCategories(restaurantId)
        }
    }
}

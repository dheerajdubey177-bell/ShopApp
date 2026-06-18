package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.data.local.SessionManager
import com.example.shop.data.repository.MenuRepository
import com.example.shop.data.repository.RestaurantRepository
import com.example.shop.data.repository.FoodRepository
import com.example.shop.data.local.entity.CategoryEntity
import com.example.shop.data.local.entity.SubCategoryEntity
import com.example.shop.data.local.entity.RestaurantEntity
import com.example.shop.data.local.entity.FoodEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RestaurantViewModel(
    private val menuRepository: MenuRepository,
    private val restaurantRepository: RestaurantRepository,
    private val foodRepository: FoodRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _restaurant = MutableStateFlow<RestaurantEntity?>(null)
    val restaurant: StateFlow<RestaurantEntity?> = _restaurant

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories

    private val _subCategories = MutableStateFlow<List<SubCategoryEntity>>(emptyList())
    val subCategories: StateFlow<List<SubCategoryEntity>> = _subCategories

    private val _foods = MutableStateFlow<List<FoodEntity>>(emptyList())
    val foods: StateFlow<List<FoodEntity>> = _foods

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            val email = sessionManager.getUser()
            if (email != null) {
                val rest = restaurantRepository.getRestaurantByOwner(email)
                if (rest != null) {
                    _restaurant.value = rest
                    _categories.value = menuRepository.getCategories(rest.id)
                    _foods.value = foodRepository.getFoodsByRestaurant(rest.id)
                }
            }
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            val restId = _restaurant.value?.id ?: return@launch
            menuRepository.addCategory(restId, name)
            _categories.value = menuRepository.getCategories(restId)
        }
    }

    fun loadSubCategories(categoryId: Int) {
        viewModelScope.launch {
            _subCategories.value = menuRepository.getSubCategories(categoryId)
        }
    }

    fun addSubCategory(categoryId: Int, name: String) {
        viewModelScope.launch {
            menuRepository.addSubCategory(categoryId, name)
            loadSubCategories(categoryId)
        }
    }
    
    fun deleteFood(food: FoodEntity) {
        viewModelScope.launch {
            foodRepository.deleteFood(food)
            _restaurant.value?.let { 
                _foods.value = foodRepository.getFoodsByRestaurant(it.id)
            }
        }
    }

    fun updateFood(food: FoodEntity) {
        viewModelScope.launch {
            foodRepository.updateFood(food)
            _restaurant.value?.let { 
                _foods.value = foodRepository.getFoodsByRestaurant(it.id)
            }
        }
    }
}

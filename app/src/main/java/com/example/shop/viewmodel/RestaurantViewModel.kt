package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.data.local.SessionManager
import com.example.shop.data.repository.MenuRepository
import com.example.shop.data.repository.RestaurantRepository
import com.example.shop.data.local.entity.CategoryEntity
import com.example.shop.data.local.entity.SubCategoryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RestaurantViewModel(
    private val menuRepository: MenuRepository,
    private val restaurantRepository: RestaurantRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories

    private val _subCategories = MutableStateFlow<List<SubCategoryEntity>>(emptyList())
    val subCategories: StateFlow<List<SubCategoryEntity>> = _subCategories

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            val email = sessionManager.getUser()
            if (email != null) {
                val restaurant = restaurantRepository.getRestaurantByOwner(email)
                if (restaurant != null) {
                    _categories.value = menuRepository.getCategories(restaurant.id)
                }
            }
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            val email = sessionManager.getUser()
            if (email != null) {
                val restaurant = restaurantRepository.getRestaurantByOwner(email)
                if (restaurant != null) {
                    menuRepository.addCategory(restaurant.id, name)
                    loadCategories()
                }
            }
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
}

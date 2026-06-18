package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.data.local.SessionManager
import com.example.shop.data.repository.FoodRepository
import com.example.shop.data.repository.MenuRepository
import com.example.shop.data.repository.RestaurantRepository
import com.example.shop.model.Food
import com.example.shop.data.local.entity.CategoryEntity
import com.example.shop.data.local.entity.SubCategoryEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddFoodViewModel(
    private val foodRepository: FoodRepository,
    private val menuRepository: MenuRepository,
    private val restaurantRepository: RestaurantRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _price = MutableStateFlow("")
    val price: StateFlow<String> = _price

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _imageUrl = MutableStateFlow("")
    val imageUrl: StateFlow<String> = _imageUrl

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId

    private val _subCategories = MutableStateFlow<List<SubCategoryEntity>>(emptyList())
    val subCategories: StateFlow<List<SubCategoryEntity>> = _subCategories

    private val _selectedSubCategoryId = MutableStateFlow<Int?>(null)
    val selectedSubCategoryId: StateFlow<Int?> = _selectedSubCategoryId

    init {
        loadCategories()
    }

    private fun loadCategories() {
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

    fun onCategorySelected(categoryId: Int) {
        _selectedCategoryId.value = categoryId
        viewModelScope.launch {
            _subCategories.value = menuRepository.getSubCategories(categoryId)
        }
    }

    fun onSubCategorySelected(subCategoryId: Int) {
        _selectedSubCategoryId.value = subCategoryId
    }

    fun onNameChange(text: String) { _name.value = text }
    fun onPriceChange(text: String) { _price.value = text }
    fun onDescriptionChange(text: String) { _description.value = text }
    fun onImageUrlChange(text: String) { _imageUrl.value = text }

    fun saveFood(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val email = sessionManager.getUser()
            if (email != null) {
                val restaurant = restaurantRepository.getRestaurantByOwner(email)
                if (restaurant != null) {
                    val foodPrice = price.value.toDoubleOrNull() ?: 0.0
                    foodRepository.addFood(
                        restaurantId = restaurant.id,
                        categoryId = _selectedCategoryId.value ?: 0,
                        subCategoryId = _selectedSubCategoryId.value ?: 0,
                        name = name.value,
                        price = foodPrice,
                        description = description.value,
                        imageUrl = _imageUrl.value
                    )
                    onSuccess()
                }
            }
        }
    }
}

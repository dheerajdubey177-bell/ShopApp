package com.example.shop.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shop.data.local.SessionManager
import com.example.shop.data.local.database.DatabaseProvider
import com.example.shop.data.repository.*

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = DatabaseProvider.getDatabase(context)
        val userRepository = UserRepository(database.userDao())
        val restaurantRepository = RestaurantRepository(database.restaurantDao())
        val foodRepository = FoodRepository(database.foodDao())
        val menuRepository = MenuRepository(database.menuDao())
        val sessionManager = SessionManager(context)

        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                AuthViewModel(userRepository, restaurantRepository, sessionManager) as T
            }
            modelClass.isAssignableFrom(AddFoodViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                AddFoodViewModel(foodRepository, menuRepository, restaurantRepository, sessionManager) as T
            }
            modelClass.isAssignableFrom(RestaurantViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                RestaurantViewModel(menuRepository, restaurantRepository, foodRepository, sessionManager) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                HomeViewModel(restaurantRepository, sessionManager) as T
            }
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                MenuViewModel(foodRepository, restaurantRepository, menuRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

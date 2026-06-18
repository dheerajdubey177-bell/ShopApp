package com.example.shop.data.repository

import com.example.shop.data.local.dao.FoodDao
import com.example.shop.data.local.entity.FoodEntity

class FoodSeeder(
    private val foodDao: FoodDao
) {

    suspend fun seedFoods() {

        foodDao.insertFood(
            FoodEntity(
                restaurantId = 1,
                categoryId = 1,
                subCategoryId = 1,
                name = "Margherita Pizza",
                description = "Classic Cheese Pizza",
                price = 299.0
            )
        )

        foodDao.insertFood(
            FoodEntity(
                restaurantId = 1,
                categoryId = 1,
                subCategoryId = 1,
                name = "Veg Burger",
                description = "Fresh Veg Burger",
                price = 149.0
            )
        )

        foodDao.insertFood(
            FoodEntity(
                restaurantId = 1,
                categoryId = 1,
                subCategoryId = 1,
                name = "White Sauce Pasta",
                description = "Creamy Pasta",
                price = 249.0
            )
        )
    }
}

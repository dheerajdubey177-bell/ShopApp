package com.example.shop.data.repository

import com.example.shop.data.local.dao.FoodDao
import com.example.shop.data.local.entity.FoodEntity

class FoodRepository(private val foodDao: FoodDao) {

    suspend fun addFood(
        restaurantId: Int,
        categoryId: Int,
        subCategoryId: Int,
        name: String,
        price: Double,
        description: String,
        imageUrl: String = ""
    ) {
        foodDao.insertFood(
            FoodEntity(
                restaurantId = restaurantId,
                categoryId = categoryId,
                subCategoryId = subCategoryId,
                name = name,
                price = price,
                description = description,
                imageUrl = imageUrl
            )
        )
    }

    suspend fun updateFood(food: FoodEntity) {
        foodDao.updateFood(food)
    }

    suspend fun deleteFood(food: FoodEntity) {
        foodDao.deleteFood(food)
    }

    suspend fun getFoodsByRestaurant(restaurantId: Int): List<FoodEntity> {
        return foodDao.getFoods().filter { it.restaurantId == restaurantId }
    }
    
    suspend fun getAllFoods(): List<FoodEntity> {
        return foodDao.getFoods()
    }
}

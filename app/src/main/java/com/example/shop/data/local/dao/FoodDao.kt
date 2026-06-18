package com.example.shop.data.local.dao

import androidx.room.*
import com.example.shop.data.local.entity.FoodEntity

@Dao
interface FoodDao {

    @Insert
    suspend fun insertFood(
        food: FoodEntity
    )

    @Query(
        "SELECT * FROM foods"
    )
    suspend fun getFoods():
            List<FoodEntity>
}

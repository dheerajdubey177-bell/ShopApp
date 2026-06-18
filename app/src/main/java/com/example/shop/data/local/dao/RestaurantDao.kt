package com.example.shop.data.local.dao

import androidx.room.*
import com.example.shop.data.local.entity.RestaurantEntity

@Dao
interface RestaurantDao {
    @Insert
    suspend fun insertRestaurant(restaurant: RestaurantEntity)

    @Query("SELECT * FROM restaurants")
    suspend fun getAllRestaurants(): List<RestaurantEntity>

    @Query("SELECT * FROM restaurants WHERE ownerEmail = :email LIMIT 1")
    suspend fun getRestaurantByOwner(email: String): RestaurantEntity?
    
    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getRestaurantById(id: Int): RestaurantEntity?
}

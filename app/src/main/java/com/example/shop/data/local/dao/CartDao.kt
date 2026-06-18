package com.example.shop.data.local.dao

import androidx.room.*
import com.example.shop.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: CartEntity)

    @Delete
    suspend fun removeFromCart(item: CartEntity)

    @Query("DELETE FROM cart_items WHERE foodId = :foodId")
    suspend fun deleteByFoodId(foodId: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
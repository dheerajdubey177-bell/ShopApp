package com.example.shop.data.local.dao

import androidx.room.*
import com.example.shop.data.local.entity.OrderEntity

@Dao
interface OrderDao {

    @Insert
    suspend fun insertOrder(
        order: OrderEntity
    )

    @Query(
        "SELECT * FROM orders"
    )
    suspend fun getOrders():
            List<OrderEntity>
}

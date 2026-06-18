package com.example.shop.data.repository

import com.example.shop.data.local.dao.OrderDao
import com.example.shop.data.local.entity.OrderEntity

class OrderRepository(
    private val dao: OrderDao
) {

    suspend fun placeOrder(
        totalAmount: Double
    ) {

        dao.insertOrder(

            OrderEntity(

                totalAmount =
                totalAmount,

                status =
                "Pending",

                createdAt =
                System.currentTimeMillis()
            )
        )
    }

    suspend fun getOrders() =
        dao.getOrders()
}

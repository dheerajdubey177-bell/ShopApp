package com.example.shop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val foodId: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String
)
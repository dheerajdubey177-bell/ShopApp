package com.example.shop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders"
)
data class OrderEntity(

    @PrimaryKey(
        autoGenerate = true
    )
    val id: Int = 0,

    val totalAmount: Double,

    val status: String,

    val createdAt: Long
)

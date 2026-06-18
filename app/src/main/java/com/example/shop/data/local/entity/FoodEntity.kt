package com.example.shop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class FoodEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val restaurantId: Int,

    val categoryId: Int,

    val subCategoryId: Int,

    val name: String,

    val price: Double,

    val description: String,
    
    val imageUrl: String = ""
)

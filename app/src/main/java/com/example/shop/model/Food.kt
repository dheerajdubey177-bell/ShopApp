package com.example.shop.model

data class Food(
    val id: Int,
    val restaurantId: Int,
    val categoryId: Int,
    val subCategoryId: Int,
    val name: String,
    val price: Double,
    val description: String,
    val imageUrl: String = ""
)

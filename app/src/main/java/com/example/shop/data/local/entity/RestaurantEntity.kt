package com.example.shop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ownerEmail: String,
    val name: String,
    val description: String,
    val address: String,
    val cuisineType: String = "Indian", // e.g., North Indian, South Indian, Chinese, Italian
    val region: String = "North",      // e.g., North, South, East, West
    val logoUrl: String = "",
    val bannerUrl: String = ""
)

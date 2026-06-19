package com.example.shop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_addresses")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userEmail: String,
    val title: String, // e.g., "Home", "Office"
    val fullAddress: String,
    val latitude: Double,
    val longitude: Double,
    val isDefault: Boolean = false
)

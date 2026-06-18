package com.example.shop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val fullName: String,

    val email: String,

    val phone: String,

    val password: String,

    val role: String = "CUSTOMER"
)

package com.example.shop.data.local.dao

import androidx.room.*
import com.example.shop.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(
        user: UserEntity
    )

    @Query(
        "SELECT * FROM users"
    )
    suspend fun getAllUsers():
            List<UserEntity>

    @Query(
        "SELECT * FROM users WHERE email = :email LIMIT 1"
    )
    suspend fun getUserByEmail(
        email: String
    ): UserEntity?
}

package com.example.shop.data.repository

import com.example.shop.data.local.dao.UserDao
import com.example.shop.data.local.entity.UserEntity

class UserRepository(
    private val userDao: UserDao
) {

    suspend fun registerUser(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        role: String = "CUSTOMER"
    ) {

        userDao.insertUser(

            UserEntity(
                fullName = fullName,
                email = email,
                phone = phone,
                password = password,
                role = role
            )
        )
    }

    suspend fun getUserByEmail(
        email: String
    ): UserEntity? {

        return userDao.getUserByEmail(
            email
        )
    }
}

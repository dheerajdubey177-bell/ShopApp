package com.example.shop.data.repository

import com.example.shop.data.local.dao.RestaurantDao
import com.example.shop.data.local.entity.RestaurantEntity

class RestaurantRepository(private val restaurantDao: RestaurantDao) {

    suspend fun createRestaurant(
        ownerEmail: String,
        name: String,
        description: String,
        address: String,
        cuisineType: String = "Indian",
        region: String = "North",
        logoUrl: String = "",
        bannerUrl: String = ""
    ) {
        restaurantDao.insertRestaurant(
            RestaurantEntity(
                ownerEmail = ownerEmail,
                name = name,
                description = description,
                address = address,
                cuisineType = cuisineType,
                region = region,
                logoUrl = logoUrl,
                bannerUrl = bannerUrl
            )
        )
    }

    suspend fun getRestaurantByOwner(email: String): RestaurantEntity? {
        return restaurantDao.getRestaurantByOwner(email)
    }

    suspend fun getAllRestaurants(): List<RestaurantEntity> {
        return restaurantDao.getAllRestaurants()
    }

    suspend fun getRestaurantById(id: Int): RestaurantEntity? {
        return restaurantDao.getRestaurantById(id)
    }
}

package com.example.shop.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shop.data.local.dao.*
import com.example.shop.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        FoodEntity::class,
        OrderEntity::class,
        RestaurantEntity::class,
        CategoryEntity::class,
        SubCategoryEntity::class
    ],
    version = 4
)
abstract class AppDatabase :
    RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun foodDao(): FoodDao

    abstract fun orderDao(): OrderDao
    
    abstract fun restaurantDao(): RestaurantDao
    
    abstract fun menuDao(): MenuDao
}

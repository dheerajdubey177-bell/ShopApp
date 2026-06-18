package com.example.shop.data.repository

import com.example.shop.data.local.dao.MenuDao
import com.example.shop.data.local.dao.RestaurantDao
import com.example.shop.data.local.dao.FoodDao
import com.example.shop.data.local.entity.RestaurantEntity
import com.example.shop.data.local.entity.CategoryEntity
import com.example.shop.data.local.entity.FoodEntity

class DataSeeder(
    private val restaurantDao: RestaurantDao,
    private val menuDao: MenuDao,
    private val foodDao: FoodDao
) {
    suspend fun seed() {
        if (restaurantDao.getAllRestaurants().isNotEmpty()) return

        // Bhilai Restaurants
        seedRestaurant(10, "Bhilai Burgers", "Best Burgers in Bhilai", "Civic Center, Bhilai", "Burger", "Bhilai", "https://images.unsplash.com/photo-1571091718767-18b5b1457add?q=80&w=800")
        seedRestaurant(11, "Bhilai Pizza", "Cheese Overloaded", "Sector 10, Bhilai", "Pizza", "Bhilai", "https://images.unsplash.com/photo-1513104890138-7c749659a591?q=80&w=800")
        seedRestaurant(12, "Bhilai Tadka", "Spicy Indian Curry", "Nehru Nagar, Bhilai", "Indian", "Bhilai", "https://images.unsplash.com/photo-1588166524941-3bf61a9c41db?q=80&w=800")

        // Pune Restaurants
        seedRestaurant(20, "Pune Pizza Hub", "Italian Delicacy", "FC Road, Pune", "Pizza", "Pune", "https://images.unsplash.com/photo-1574071318508-1cdbcd80ad55?q=80&w=800")
        seedRestaurant(21, "Pune Chai", "Refreshment Spot", "Kothrud, Pune", "Coffee", "Pune", "https://images.unsplash.com/photo-1544787210-28272550d795?q=80&w=800")

        // Mumbai Restaurants
        seedRestaurant(30, "Mumbai Munchies", "Street Food Style", "Andheri, Mumbai", "Fast Food", "Mumbai", "https://images.unsplash.com/photo-1601050633647-81a35137d289?q=80&w=800")
    }

    private suspend fun seedRestaurant(id: Int, name: String, desc: String, addr: String, cuisine: String, region: String, banner: String) {
        restaurantDao.insertRestaurant(
            RestaurantEntity(
                id = id,
                ownerEmail = "owner$id@shop.com",
                name = name,
                description = desc,
                address = addr,
                cuisineType = cuisine,
                region = region,
                bannerUrl = banner
            )
        )
        menuDao.insertCategory(CategoryEntity(id = id, restaurantId = id, name = cuisine))
        foodDao.insertFood(
            FoodEntity(
                restaurantId = id,
                categoryId = id,
                subCategoryId = 0,
                name = "Classic $cuisine",
                price = 199.0,
                description = "Delicious $name special",
                imageUrl = banner
            )
        )
    }
}

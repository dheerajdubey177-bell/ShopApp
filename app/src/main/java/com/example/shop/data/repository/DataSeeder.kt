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

        // 1. Pizza Hut
        restaurantDao.insertRestaurant(
            RestaurantEntity(
                id = 1,
                ownerEmail = "owner@pizzahut.com",
                name = "Pizza Hut",
                description = "Classic Pizzas & Wings",
                address = "Downtown, Pune",
                bannerUrl = "https://images.unsplash.com/photo-1513104890138-7c749659a591?q=80&w=800",
                logoUrl = "https://www.pizzahut.co.in/assets/img/logo.png"
            )
        )
        menuDao.insertCategory(CategoryEntity(id = 1, restaurantId = 1, name = "Veg Pizza"))
        foodDao.insertFood(FoodEntity(restaurantId = 1, categoryId = 1, subCategoryId = 0, name = "Margherita", price = 299.0, description = "Cheese & Tomato", imageUrl = "https://images.unsplash.com/photo-1574071318508-1cdbcd80ad55?q=80&w=400"))
        foodDao.insertFood(FoodEntity(restaurantId = 1, categoryId = 1, subCategoryId = 0, name = "Farmhouse", price = 399.0, description = "Mushroom, Onion, Tomato", imageUrl = "https://images.unsplash.com/photo-1571407970349-bc81e7e96d47?q=80&w=400"))

        // 2. Burger King
        restaurantDao.insertRestaurant(
            RestaurantEntity(
                id = 2,
                ownerEmail = "owner@bk.com",
                name = "Burger King",
                description = "Home of the Whopper",
                address = "Main Street, Mumbai",
                bannerUrl = "https://images.unsplash.com/photo-1571091718767-18b5b1457add?q=80&w=800",
                logoUrl = "https://www.burgerking.in/static/media/logo.77e48232.png"
            )
        )
        menuDao.insertCategory(CategoryEntity(id = 2, restaurantId = 2, name = "Burgers"))
        foodDao.insertFood(FoodEntity(restaurantId = 2, categoryId = 2, subCategoryId = 0, name = "Whopper Jr", price = 149.0, description = "Classic flame-grilled burger", imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=400"))

        // 3. Daal Bhaat House
        restaurantDao.insertRestaurant(
            RestaurantEntity(
                id = 3,
                ownerEmail = "owner@daalbhaat.com",
                name = "Daal Bhaat House",
                description = "Authentic Indian Comfort Food",
                address = "Local Market, Delhi",
                bannerUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?q=80&w=800",
                logoUrl = ""
            )
        )
        menuDao.insertCategory(CategoryEntity(id = 3, restaurantId = 3, name = "Thalis"))
        foodDao.insertFood(FoodEntity(restaurantId = 3, categoryId = 3, subCategoryId = 0, name = "Special Daal Bhaat", price = 199.0, description = "Homemade style daal rice", imageUrl = "https://images.unsplash.com/photo-1589302168068-964664d93dc0?q=80&w=400"))
    }
}

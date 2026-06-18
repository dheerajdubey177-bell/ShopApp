package com.example.shop.data.repository

import com.example.shop.data.local.dao.MenuDao
import com.example.shop.data.local.entity.CategoryEntity
import com.example.shop.data.local.entity.SubCategoryEntity

class MenuRepository(private val menuDao: MenuDao) {

    suspend fun addCategory(restaurantId: Int, name: String) {
        menuDao.insertCategory(CategoryEntity(restaurantId = restaurantId, name = name))
    }

    suspend fun getCategories(restaurantId: Int): List<CategoryEntity> {
        return menuDao.getCategoriesByRestaurant(restaurantId)
    }

    suspend fun addSubCategory(categoryId: Int, name: String) {
        menuDao.insertSubCategory(SubCategoryEntity(categoryId = categoryId, name = name))
    }

    suspend fun getSubCategories(categoryId: Int): List<SubCategoryEntity> {
        return menuDao.getSubCategoriesByCategory(categoryId)
    }
}

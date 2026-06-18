package com.example.shop.data.local.dao

import androidx.room.*
import com.example.shop.data.local.entity.CategoryEntity
import com.example.shop.data.local.entity.SubCategoryEntity

@Dao
interface MenuDao {
    @Insert
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM categories WHERE restaurantId = :restaurantId")
    suspend fun getCategoriesByRestaurant(restaurantId: Int): List<CategoryEntity>

    @Insert
    suspend fun insertSubCategory(subCategory: SubCategoryEntity)

    @Query("SELECT * FROM subcategories WHERE categoryId = :categoryId")
    suspend fun getSubCategoriesByCategory(categoryId: Int): List<SubCategoryEntity>
}

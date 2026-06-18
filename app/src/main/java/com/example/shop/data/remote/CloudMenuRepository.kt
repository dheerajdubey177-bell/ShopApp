package com.example.shop.data.remote

import com.example.shop.data.local.entity.CategoryEntity
import com.example.shop.data.local.entity.SubCategoryEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CloudMenuRepository(private val db: FirebaseFirestore) {
    private val categoryCollection = db.collection("categories")
    private val subCategoryCollection = db.collection("subcategories")

    suspend fun syncCategory(category: CategoryEntity) {
        categoryCollection.document(category.id.toString()).set(category).await()
    }

    suspend fun getCategoriesByRestaurant(restaurantId: Int): List<CategoryEntity> {
        return try {
            val snapshot = categoryCollection
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .await()
            snapshot.toObjects(CategoryEntity::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun syncSubCategory(subCategory: SubCategoryEntity) {
        subCategoryCollection.document(subCategory.id.toString()).set(subCategory).await()
    }

    suspend fun getSubCategoriesByCategory(categoryId: Int): List<SubCategoryEntity> {
        return try {
            val snapshot = subCategoryCollection
                .whereEqualTo("categoryId", categoryId)
                .get()
                .await()
            snapshot.toObjects(SubCategoryEntity::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}

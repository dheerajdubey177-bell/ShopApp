package com.example.shop.data.remote

import com.example.shop.data.local.entity.RestaurantEntity
import com.example.shop.data.local.entity.CategoryEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CloudRestaurantRepository(private val db: FirebaseFirestore) {
    private val restCollection = db.collection("restaurants")

    suspend fun syncRestaurant(restaurant: RestaurantEntity) {
        restCollection.document(restaurant.id.toString()).set(restaurant).await()
    }

    suspend fun addCategory(category: CategoryEntity) {
        db.collection("categories")
            .document(category.id.toString())
            .set(category)
            .await()
    }

    suspend fun getAllRestaurants(): List<RestaurantEntity> {
        return try {
            val snapshot = restCollection.get().await()
            snapshot.toObjects(RestaurantEntity::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}

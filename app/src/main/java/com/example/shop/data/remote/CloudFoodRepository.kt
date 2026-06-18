package com.example.shop.data.remote

import com.example.shop.data.local.entity.FoodEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CloudFoodRepository(private val db: FirebaseFirestore) {
    private val foodCollection = db.collection("foods")

    suspend fun syncFood(food: FoodEntity) {
        foodCollection.document(food.id.toString()).set(food).await()
    }

    suspend fun getFoodsByRestaurant(restaurantId: Int): List<FoodEntity> {
        return try {
            val snapshot = foodCollection
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .await()
            snapshot.toObjects(FoodEntity::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteFood(id: Int) {
        foodCollection.document(id.toString()).delete().await()
    }
}

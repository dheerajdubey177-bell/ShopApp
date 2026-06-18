package com.example.shop.data.remote

import com.example.shop.data.local.entity.UserEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CloudUserRepository(private val db: FirebaseFirestore) {
    private val userCollection = db.collection("users")

    suspend fun syncUser(user: UserEntity) {
        userCollection.document(user.email).set(
            mapOf(
                "fullName" to user.fullName,
                "email" to user.email,
                "phone" to user.phone,
                "role" to user.role
            )
        ).await()
    }

    suspend fun getUserByEmail(email: String): Map<String, Any>? {
        return try {
            val doc = userCollection.document(email).get().await()
            if (doc.exists()) doc.data else null
        } catch (e: Exception) {
            null
        }
    }
}

package com.example.shop.data.remote

import com.example.shop.data.local.entity.OrderEntity
import com.example.shop.data.local.entity.OrderItemEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CloudOrderRepository(private val db: FirebaseFirestore) {
    private val orderCollection = db.collection("orders")

    suspend fun placeOrder(order: OrderEntity, items: List<OrderItemEntity>) {
        val orderRef = orderCollection.document(order.id.toString())
        
        db.runBatch { batch ->
            batch.set(orderRef, order)
            items.forEach { item ->
                val itemRef = orderRef.collection("items").document(item.id.toString())
                batch.set(itemRef, item)
            }
        }.await()
    }

    suspend fun updateStatus(orderId: Int, status: String) {
        orderCollection.document(orderId.toString())
            .update("status", status)
            .await()
    }

    suspend fun getOrdersByRestaurant(restaurantId: Int): List<OrderEntity> {
        return try {
            val snapshot = orderCollection
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .await()
            snapshot.toObjects(OrderEntity::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}

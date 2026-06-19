package com.example.shop.data.remote

import com.example.shop.data.local.entity.AddressEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CloudAddressRepository(private val db: FirebaseFirestore) {
    private val addressCollection = db.collection("addresses")

    suspend fun syncAddress(address: AddressEntity) {
        val docId = if (address.id == 0) null else address.id.toString()
        val docRef = if (docId != null) addressCollection.document(docId) else addressCollection.document()
        
        docRef.set(
            mapOf(
                "userEmail" to address.userEmail,
                "title" to address.title,
                "fullAddress" to address.fullAddress,
                "latitude" to address.latitude,
                "longitude" to address.longitude,
                "isDefault" to address.isDefault
            )
        ).await()
    }

    suspend fun deleteAddress(addressId: Int) {
        addressCollection.document(addressId.toString()).delete().await()
    }
}

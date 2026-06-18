package com.example.shop.model

data class DeliveryOrder(
    val orderId: Int,
    val customerName: String,
    val address: String,
    val amount: Double,
    val status: String
)
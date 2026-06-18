package com.example.shop.model

data class Order(
    val orderId: Int,
    val items: List<String>,
    val totalAmount: Double,
    val status: String
)
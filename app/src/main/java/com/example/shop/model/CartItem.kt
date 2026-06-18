package com.example.shop.model

data class CartItem(
    val food: Food,
    var quantity: Int = 1
)
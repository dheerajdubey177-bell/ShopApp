package com.example.shop.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.shop.model.CartItem
import com.example.shop.model.Food

object CartRepository {

    val cartItems = mutableStateListOf<CartItem>()

    fun addItem(food: Food) {
        val index = cartItems.indexOfFirst { it.food.id == food.id }
        if (index != -1) {
            val item = cartItems[index]
            cartItems[index] = item.copy(quantity = item.quantity + 1)
        } else {
            cartItems.add(CartItem(food, 1))
        }
    }

    fun incrementQuantity(foodId: Int) {
        val index = cartItems.indexOfFirst { it.food.id == foodId }
        if (index != -1) {
            val item = cartItems[index]
            cartItems[index] = item.copy(quantity = item.quantity + 1)
        }
    }

    fun decrementQuantity(foodId: Int) {
        val index = cartItems.indexOfFirst { it.food.id == foodId }
        if (index != -1) {
            val item = cartItems[index]
            if (item.quantity > 1) {
                cartItems[index] = item.copy(quantity = item.quantity - 1)
            } else {
                cartItems.removeAt(index)
            }
        }
    }

    fun removeItem(food: Food) {
        cartItems.removeAll { it.food.id == food.id }
    }

    fun getTotal(): Double {
        return cartItems.sumOf { it.food.price * it.quantity }
    }
}
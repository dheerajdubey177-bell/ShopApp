package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.data.local.SessionManager
import com.example.shop.data.repository.OrderRepository
import com.example.shop.data.repository.RestaurantRepository
import com.example.shop.data.repository.CartRepository
import com.example.shop.data.local.entity.OrderEntity
import com.example.shop.data.local.entity.OrderItemEntity
import com.example.shop.model.OrderStatus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository,
    private val restaurantRepository: RestaurantRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<OrderUiEvent>()
    val uiEvent: SharedFlow<OrderUiEvent> = _uiEvent

    private val _customerOrders = MutableStateFlow<List<OrderEntity>>(emptyList())
    val customerOrders: StateFlow<List<OrderEntity>> = _customerOrders

    private val _restaurantOrders = MutableStateFlow<List<OrderEntity>>(emptyList())
    val restaurantOrders: StateFlow<List<OrderEntity>> = _restaurantOrders

    fun placeOrder(address: String) {
        viewModelScope.launch {
            val email = sessionManager.getUser() ?: return@launch
            // For simplicity, we get name from email or dummy. In real app, from profile.
            val name = email.split("@")[0]
            val phone = "9999999999"
            
            val items = CartRepository.cartItems.toList()
            if (items.isEmpty()) return@launch

            val restaurantId = items[0].food.restaurantId
            val total = CartRepository.getTotal()

            val success = orderRepository.placeOrder(
                customerEmail = email,
                restaurantId = restaurantId,
                customerName = name,
                customerPhone = phone,
                deliveryAddress = address,
                totalAmount = total,
                items = items
            )

            if (success) {
                CartRepository.cartItems.clear()
                _uiEvent.emit(OrderUiEvent.Success("Order placed successfully!"))
            } else {
                _uiEvent.emit(OrderUiEvent.Error("Failed to place order."))
            }
        }
    }

    fun loadCustomerOrders() {
        viewModelScope.launch {
            val email = sessionManager.getUser() ?: return@launch
            _customerOrders.value = orderRepository.getCustomerOrders(email)
        }
    }

    fun loadRestaurantOrders() {
        viewModelScope.launch {
            val email = sessionManager.getUser() ?: return@launch
            val restaurant = restaurantRepository.getRestaurantByOwner(email)
            if (restaurant != null) {
                _restaurantOrders.value = orderRepository.getRestaurantOrders(restaurant.id)
            }
        }
    }

    fun updateStatus(orderId: Int, status: OrderStatus) {
        viewModelScope.launch {
            orderRepository.updateOrderStatus(orderId, status)
            loadRestaurantOrders()
        }
    }
}

sealed class OrderUiEvent {
    data class Success(val message: String) : OrderUiEvent()
    data class Error(val message: String) : OrderUiEvent()
}

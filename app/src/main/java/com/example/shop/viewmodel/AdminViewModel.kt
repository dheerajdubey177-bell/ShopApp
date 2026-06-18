package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.data.repository.OrderRepository
import com.example.shop.data.local.entity.OrderEntity
import com.example.shop.model.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AdminViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    private val _allOrders = MutableStateFlow<List<OrderEntity>>(emptyList())
    val allOrders: StateFlow<List<OrderEntity>> = _allOrders

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _allOrders.value = orderRepository.getAllOrders()
        }
    }

    fun getPlatformProfit(): Double {
        return _allOrders.value.filter { it.status == OrderStatus.DELIVERED.name }.sumOf { it.platformFee }
    }

    fun getOrdersByDate(dateStr: String): List<OrderEntity> {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return _allOrders.value.filter { 
            val orderDate = sdf.format(Date(it.createdAt))
            orderDate == dateStr
        }
    }

    fun getOrdersByMonth(monthYearStr: String): List<OrderEntity> {
        val sdf = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        return _allOrders.value.filter { 
            val orderMonth = sdf.format(Date(it.createdAt))
            orderMonth == monthYearStr
        }
    }

    fun getOrdersByLocation(city: String): List<OrderEntity> {
        return _allOrders.value.filter { it.city.equals(city, ignoreCase = true) }
    }
}

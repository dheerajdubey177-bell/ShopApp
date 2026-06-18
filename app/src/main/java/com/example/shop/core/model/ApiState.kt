package com.example.shop.core.model

data class ApiState<T>(
    val data: T? = null,
    val loading: Boolean = false,
    val error: String? = null
)

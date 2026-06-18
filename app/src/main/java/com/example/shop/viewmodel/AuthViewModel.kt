package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.data.local.SessionManager
import com.example.shop.data.repository.UserRepository
import com.example.shop.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userRepository: UserRepository,
    private val restaurantRepository: RestaurantRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent: SharedFlow<AuthUiEvent> = _uiEvent

    fun register(
        fullName: String, 
        email: String, 
        phone: String, 
        password: String, 
        role: String,
        restaurantName: String = "",
        restaurantDesc: String = "",
        restaurantAddress: String = "",
        cuisineType: String = "Indian",
        region: String = "North",
        logoUrl: String = "",
        bannerUrl: String = ""
    ) {
        viewModelScope.launch {
            try {
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    _uiEvent.emit(AuthUiEvent.Error("User already exists"))
                    return@launch
                }
                userRepository.registerUser(fullName, email, phone, password, role)
                
                if (role == "RESTAURANT_OWNER") {
                    restaurantRepository.createRestaurant(
                        ownerEmail = email,
                        name = restaurantName,
                        description = restaurantDesc,
                        address = restaurantAddress,
                        cuisineType = cuisineType,
                        region = region,
                        logoUrl = logoUrl,
                        bannerUrl = bannerUrl
                    )
                }

                _uiEvent.emit(AuthUiEvent.Success("Registration Successful"))
            } catch (e: Exception) {
                _uiEvent.emit(AuthUiEvent.Error("Registration failed: ${e.message}"))
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserByEmail(email)
                if (user != null && user.password == password) {
                    sessionManager.saveUser(email, user.role)
                    _uiEvent.emit(AuthUiEvent.LoginSuccess(user.role))
                } else {
                    _uiEvent.emit(AuthUiEvent.Error("Invalid email or password"))
                }
            } catch (e: Exception) {
                _uiEvent.emit(AuthUiEvent.Error("Login failed: ${e.message}"))
            }
        }
    }
}

sealed class AuthUiEvent {
    data class Success(val message: String) : AuthUiEvent()
    data class Error(val message: String) : AuthUiEvent()
    data class LoginSuccess(val role: String) : AuthUiEvent()
}

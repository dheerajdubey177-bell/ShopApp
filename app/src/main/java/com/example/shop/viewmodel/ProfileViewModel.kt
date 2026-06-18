package com.example.shop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.data.local.SessionManager
import com.example.shop.data.repository.UserRepository
import com.example.shop.data.local.entity.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            val email = sessionManager.getUser()
            if (email != null) {
                _user.value = userRepository.getUserByEmail(email)
            }
        }
    }
}

package com.example.shop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.shop.config.AppConfig
import com.example.shop.data.local.SessionManager
import com.example.shop.data.local.database.DatabaseProvider
import com.example.shop.data.repository.DataSeeder
import com.example.shop.data.repository.UserLocationManager
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun SplashScreen(
    onFinish: (isLoggedIn: Boolean, role: String) -> Unit,
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val database = remember { DatabaseProvider.getDatabase(context) }

    LaunchedEffect(Unit) {
        // Seed dummy data
        DataSeeder(database.restaurantDao(), database.menuDao(), database.foodDao()).seed()
        
        // Initialize global location from session if it exists
        val savedLocation = sessionManager.getLocation()
        if (savedLocation != null) {
            UserLocationManager.updateLocation(savedLocation)
        }
        
        delay(3.seconds)
        val userEmail = sessionManager.getUser()
        val role = sessionManager.getRole() ?: "CUSTOMER"
        if (userEmail != null) {
            onFinish(true, role)
        } else {
            onFinish(false, "CUSTOMER")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "🍔",
                fontSize = 80.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = AppConfig.APP_NAME,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            CircularProgressIndicator()
        }
    }
}

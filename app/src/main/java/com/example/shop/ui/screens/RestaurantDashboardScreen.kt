package com.example.shop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDashboardScreen(
    onLogout: () -> Unit,
    onManageCategories: () -> Unit,
    onManageSubCategories: () -> Unit,
    onManageFoodItems: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Restaurant Dashboard") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DashboardCard("Manage Categories", "📁", onManageCategories)
            }
            item {
                DashboardCard("Manage Sub-Categories", "📂", onManageSubCategories)
            }
            item {
                DashboardCard("Manage Food Items", "🍔", onManageFoodItems)
            }
            item {
                DashboardCard("View Orders", "📦", {})
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Logout")
                }
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, icon: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(icon)
            Text(title)
        }
    }
}

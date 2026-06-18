package com.example.shop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.model.OrderStatus
import com.example.shop.viewmodel.OrderViewModel
import com.example.shop.viewmodel.ViewModelFactory
import com.example.shop.data.repository.UserLocationManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryDashboardScreen(
    onLogout: () -> Unit,
    viewModel: OrderViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val allOrders by viewModel.allOrders.collectAsState()
    val city by UserLocationManager.currentLocation.collectAsState()

    // Filter orders that are READY for pickup or already with this rider in this city
    val availableOrders = allOrders.filter { 
        it.city.equals(city, ignoreCase = true) && 
        (it.status == OrderStatus.READY.name || it.status == OrderStatus.OUT_FOR_DELIVERY.name)
    }

    val earnings = allOrders.filter { it.status == OrderStatus.DELIVERED.name }.size * 40.0

    LaunchedEffect(Unit) {
        viewModel.loadAllOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Delivery Partner", style = MaterialTheme.typography.titleSmall)
                        Text(city, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) { Text("🚪") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Your Total Earnings", style = MaterialTheme.typography.titleMedium)
                        Text("₹${String.format("%.2f", earnings)}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("at ₹40.00 per delivery", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            item {
                Text("Active Tasks", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }

            if (availableOrders.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No orders ready for delivery in $city.")
                    }
                }
            }

            items(availableOrders) { order ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Order #${order.id}", fontWeight = FontWeight.Bold)
                            Text(order.status, color = MaterialTheme.colorScheme.primary)
                        }
                        Text("Customer: ${order.customerName}")
                        Text("Drop-off: ${order.deliveryAddress}")
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        if (order.status == OrderStatus.READY.name) {
                            Button(
                                onClick = { viewModel.updateStatus(order.id, OrderStatus.OUT_FOR_DELIVERY) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Pick Up Order")
                            }
                        } else if (order.status == OrderStatus.OUT_FOR_DELIVERY.name) {
                            Button(
                                onClick = { viewModel.updateStatus(order.id, OrderStatus.DELIVERED) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                            ) {
                                Text("Mark as Delivered")
                            }
                        }
                    }
                }
            }
        }
    }
}

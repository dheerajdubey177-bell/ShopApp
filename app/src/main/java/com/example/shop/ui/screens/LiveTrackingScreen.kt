package com.example.shop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.data.local.entity.OrderEntity
import com.example.shop.data.local.entity.RestaurantEntity
import com.example.shop.viewmodel.OrderViewModel
import com.example.shop.viewmodel.RestaurantViewModel
import com.example.shop.viewmodel.ViewModelFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTrackingScreen(
    orderId: Int,
    onBack: () -> Unit,
    orderViewModel: OrderViewModel = viewModel(factory = ViewModelFactory(LocalContext.current)),
    restaurantViewModel: RestaurantViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val customerOrders by orderViewModel.customerOrders.collectAsState()
    val order = customerOrders.find { it.id == orderId }
    val [restaurant, setRestaurant] = remember { mutableStateOf<RestaurantEntity?>(null) }
    
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(order) {
        order?.let {
            val res = restaurantViewModel.getRestaurantById(it.restaurantId)
            setRestaurant(res)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Tracking") },
                navigationIcon = { IconButton(onClick = onBack) { Text("⬅️") } }
            )
        }
    ) { padding ->
        if (order == null || restaurant == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val restaurantLatLng = LatLng(restaurant.latitude, restaurant.longitude)
            val deliveryLatLng = LatLng(order.latitude, order.longitude)
            
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(deliveryLatLng, 14f)
            }

            Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        Marker(
                            state = rememberMarkerState(position = restaurantLatLng),
                            title = restaurant.name,
                            snippet = "Pickup Point"
                        )
                        Marker(
                            state = rememberMarkerState(position = deliveryLatLng),
                            title = "You",
                            snippet = "Delivery Point"
                        )
                        
                        // Polyline could be added here for the route
                    }
                }
                
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (order.status == "OUT_FOR_DELIVERY") "Rider is on the way!" else "Preparing your food...",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text("Order Status: ${order.status}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Restaurant: ${restaurant.name}", fontWeight = FontWeight.Bold)
                        Text(restaurant.address, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

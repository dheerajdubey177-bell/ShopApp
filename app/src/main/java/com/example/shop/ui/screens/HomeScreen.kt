package com.example.shop.ui.screens

import android.Manifest
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shop.ui.components.OfferBanner
import com.example.shop.viewmodel.HomeViewModel
import com.example.shop.viewmodel.ViewModelFactory
import com.example.shop.utils.LocationHelper
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    onRestaurantClick: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current
    val restaurants by viewModel.filteredRestaurantsState.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()

    var showLocationDialog by remember { mutableStateOf(userLocation.isBlank()) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_COARSE_LOCATION)

    val mainCategories = listOf(
        Pair("Pizza", "🍕"),
        Pair("Burger", "🍔"),
        Pair("Pasta", "🍝"),
        Pair("Dessert", "🍰"),
        Pair("Indian", "🍛"),
        Pair("Coffee", "☕")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { showLocationDialog = true }
                    ) {
                        Text("FoodExpress", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
                        Text(if (userLocation.isNotBlank()) "$userLocation 📍" else "Select Location 📍", style = MaterialTheme.typography.labelSmall)
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { viewModel.onSearchTextChange(it) },
                    placeholder = { Text("Search for food, restaurants...") },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = MaterialTheme.shapes.medium,
                    leadingIcon = { Text("🔍") },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                )
            }

            item {
                OfferBanner(
                    title = "🔥 50% OFF",
                    subtitle = if (userLocation.isNotBlank()) "First order in $userLocation" else "First order on all Restaurants"
                )
            }

            item {
                Text(
                    text = "What's on your mind?",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(16.dp)
                )
                
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(mainCategories) { category ->
                        val isSelected = selectedCategory == category.first
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(70.dp)
                                .clickable { viewModel.onCategorySelect(category.first) }
                        ) {
                            Surface(
                                shape = MaterialTheme.shapes.extraLarge,
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.size(60.dp),
                                border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(category.second, style = MaterialTheme.typography.headlineMedium)
                                }
                            }
                            Text(
                                category.first, 
                                style = MaterialTheme.typography.labelMedium, 
                                modifier = Modifier.padding(top = 4.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val locationText = if (userLocation.isNotBlank()) "in $userLocation" else ""
                    Text(
                        text = if (selectedCategory != null) "$selectedCategory $locationText" else "Top Restaurants $locationText",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            if (restaurants.isEmpty()) {
                item {
                    val locationText = if (userLocation.isNotBlank()) "in $userLocation" else "here"
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No restaurants found $locationText.", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { showLocationDialog = true }) {
                            Text("Try Another Location")
                        }
                    }
                }
            }

            items(restaurants) { restaurant ->
                RestaurantCard(
                    name = restaurant.name,
                    desc = restaurant.description,
                    bannerUrl = restaurant.bannerUrl
                ) {
                    onRestaurantClick(restaurant.id)
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (showLocationDialog) {
            LocationSelectionDialog(
                currentLocation = userLocation,
                onLocationSelected = { 
                    viewModel.onLocationChange(it)
                    showLocationDialog = false
                },
                onAutoDetect = {
                    if (locationPermissionState.status.isGranted) {
                        LocationHelper.getCurrentLocation(context) { nearestCity ->
                            if (nearestCity != null) {
                                viewModel.onLocationChange(nearestCity)
                                showLocationDialog = false
                            } else {
                                // Fallback or toast
                            }
                        }
                    } else {
                        locationPermissionState.launchPermissionRequest()
                    }
                },
                onDismiss = { if (userLocation.isNotBlank()) showLocationDialog = false }
            )
        }
    }
}

@Composable
fun LocationSelectionDialog(
    currentLocation: String,
    onLocationSelected: (String) -> Unit,
    onAutoDetect: () -> Unit,
    onDismiss: () -> Unit
) {
    val locations = listOf("Bhilai", "Raipur", "Pune", "Mumbai", "Delhi", "Bangalore", "Kolkata")
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Your City") },
        text = {
            Column {
                Button(
                    onClick = onAutoDetect,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
                ) {
                    Text("📍 Use My Current Location")
                }
                
                Text("Popular Cities", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    items(locations) { location ->
                        TextButton(
                            onClick = { onLocationSelected(location) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                location, 
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (location == currentLocation) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
             TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantCard(name: String, desc: String, bannerUrl: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = if (bannerUrl.isNotEmpty()) bannerUrl else "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?q=80&w=800",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.padding(12.dp).align(Alignment.TopEnd),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text("⭐ 4.5", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelMedium)
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🕒 25-30 mins", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("• Free Delivery", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

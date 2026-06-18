package com.example.shop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shop.viewmodel.HomeViewModel
import com.example.shop.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantsScreen(
    onRestaurantClick: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val restaurants by viewModel.restaurants.collectAsState()
    var searchText by remember { mutableStateOf("") }

    val regions = listOf("All", "Pune West", "Pune East", "Mumbai Express", "Delhi")
    val cuisines = listOf("All", "Italian", "Indian", "Chinese", "Fast Food")

    var selectedRegion by remember { mutableStateOf("All") }
    var selectedCuisine by remember { mutableStateOf("All") }

    val filteredRestaurants = restaurants.filter { rest ->
        val matchesSearch = rest.name.contains(searchText, ignoreCase = true)
        val matchesRegion = selectedRegion == "All" || rest.address.contains(selectedRegion, ignoreCase = true)
        // Cuisine filtering is a bit harder as it's currently in description, but we can try:
        val matchesCuisine = selectedCuisine == "All" || rest.description.contains(selectedCuisine, ignoreCase = true)
        
        matchesSearch && matchesRegion && matchesCuisine
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore") }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Search restaurants or cuisines") },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = MaterialTheme.shapes.medium,
                    leadingIcon = { Text("🔍") }
                )
            }

            item {
                Text(
                    text = "Filter by Region",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(regions) { region ->
                        FilterChip(
                            selected = selectedRegion == region,
                            onClick = { selectedRegion = region },
                            label = { Text(region) }
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Filter by Cuisine",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cuisines) { cuisine ->
                        SuggestionChip(
                            onClick = { selectedCuisine = cuisine },
                            label = { Text(cuisine) },
                            colors = if(selectedCuisine == cuisine) SuggestionChipDefaults.suggestionChipColors(containerColor = MaterialTheme.colorScheme.primaryContainer) else SuggestionChipDefaults.suggestionChipColors()
                        )
                    }
                }
                Divider(modifier = Modifier.padding(top = 8.dp))
            }

            if (filteredRestaurants.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("No restaurants found", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            items(filteredRestaurants) { restaurant ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    onClick = { onRestaurantClick(restaurant.id) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                            AsyncImage(
                                model = if (restaurant.bannerUrl.isNotEmpty()) restaurant.bannerUrl else "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?q=80&w=800",
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    restaurant.name,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text("⭐ 4.5", style = MaterialTheme.typography.labelLarge)
                            }
                            Text(
                                restaurant.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text(
                                    "📍 ${restaurant.address}",
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Text(
                                    "🕒 25-30 mins",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

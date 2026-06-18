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
    val restaurants by viewModel.filteredRestaurantsState.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val cuisines = listOf("All", "Indian", "Pizza", "Burger", "Coffee", "Chinese", "Italian", "Fast Food")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore in $userLocation") }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { viewModel.onSearchTextChange(it) },
                    label = { Text("Search for restaurants, food...") },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = MaterialTheme.shapes.medium,
                    leadingIcon = { Text("🔍") }
                )
            }

            item {
                Text(
                    text = "Cuisine",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cuisines) { cuisine ->
                        val isSelected = if (cuisine == "All") selectedCategory == null else selectedCategory == cuisine
                        SuggestionChip(
                            onClick = { 
                                if (cuisine == "All") viewModel.onCategorySelect(null)
                                else viewModel.onCategorySelect(cuisine)
                            },
                            label = { Text(cuisine) },
                            colors = if(isSelected) SuggestionChipDefaults.suggestionChipColors(containerColor = MaterialTheme.colorScheme.primaryContainer) else SuggestionChipDefaults.suggestionChipColors()
                        )
                    }
                }
                Divider(modifier = Modifier.padding(top = 8.dp))
            }

            if (restaurants.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text("No restaurants match your filters in $userLocation.", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Try clearing filters or changing location.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            items(restaurants) { restaurant ->
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
                            Surface(
                                modifier = Modifier.padding(12.dp).align(androidx.compose.ui.Alignment.TopEnd),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(restaurant.region, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    restaurant.name,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.small) {
                                    Text(restaurant.cuisineType, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall)
                                }
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
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.example.shop.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shop.data.repository.CartRepository
import com.example.shop.ui.components.FoodCard
import com.example.shop.viewmodel.MenuViewModel
import com.example.shop.viewmodel.ViewModelFactory
import com.example.shop.model.Food

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    restaurantId: Int = 1,
    viewModel: MenuViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current
    val restaurant by viewModel.restaurant.collectAsState()
    val foods by viewModel.foods.collectAsState()
    val categories by viewModel.categories.collectAsState()
    
    var searchText by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(restaurantId) {
        viewModel.loadMenu(restaurantId)
    }

    val filteredFoods = foods.filter {
        val matchesSearch = it.name.contains(searchText, ignoreCase = true)
        val matchesCategory = selectedCategoryId == null || it.categoryId == selectedCategoryId
        matchesSearch && matchesCategory
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(restaurant?.name ?: "Menu", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            restaurant?.let { rest ->
                Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                    AsyncImage(
                        model = if (rest.bannerUrl.isNotEmpty()) rest.bannerUrl else "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?q=80&w=800",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Search items...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    leadingIcon = { Text("🔍") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (categories.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedCategoryId == null,
                                onClick = { selectedCategoryId = null },
                                label = { Text("All") }
                            )
                        }
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategoryId == category.id,
                                onClick = { selectedCategoryId = category.id },
                                label = { Text(category.name) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filteredFoods) { foodEntity ->
                        val food = Food(
                            id = foodEntity.id,
                            restaurantId = foodEntity.restaurantId,
                            categoryId = foodEntity.categoryId,
                            subCategoryId = foodEntity.subCategoryId,
                            name = foodEntity.name,
                            price = foodEntity.price,
                            description = foodEntity.description,
                            imageUrl = foodEntity.imageUrl
                        )
                        FoodCard(
                            food = food,
                            onAddToCart = {
                                CartRepository.addItem(food)
                                Toast.makeText(context, "${food.name} added to cart", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                    
                    if (filteredFoods.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
                                Text("No items found", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.example.shop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.data.local.entity.CategoryEntity
import com.example.shop.data.local.entity.FoodEntity
import com.example.shop.viewmodel.RestaurantViewModel
import com.example.shop.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDashboardScreen(
    onLogout: () -> Unit,
    onManageCategories: () -> Unit,
    onManageSubCategories: () -> Unit,
    onManageFoodItems: () -> Unit,
    viewModel: RestaurantViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val restaurant by viewModel.restaurant.collectAsState()
    val foods by viewModel.foods.collectAsState()
    val categories by viewModel.categories.collectAsState()
    
    var showEditDialog by remember { mutableStateOf<FoodEntity?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadData()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Dashboard", style = MaterialTheme.typography.titleSmall)
                        Text(restaurant?.name ?: "Loading...", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Text("🚪")
                    }
                }
            )
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
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DashboardSmallCard("Categories", "📁", onManageCategories, Modifier.weight(1f))
                    DashboardSmallCard("Add Food", "➕", onManageFoodItems, Modifier.weight(1f))
                }
            }

            item {
                Text("Your Current Menu", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }

            if (foods.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No food items added yet.")
                    }
                }
            }

            items(foods) { food ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(food.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("₹${food.price}", color = MaterialTheme.colorScheme.primary)
                            val categoryName = categories.find { it.id == food.categoryId }?.name ?: "Unknown"
                            Text("Category: $categoryName", style = MaterialTheme.typography.labelSmall)
                            Text(food.description, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                        }
                        Row {
                            IconButton(onClick = { showEditDialog = food }) {
                                Text("✏️")
                            }
                            IconButton(onClick = { viewModel.deleteFood(food) }) {
                                Text("🗑️")
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        showEditDialog?.let { food ->
            EditFoodDialog(
                food = food,
                categories = categories,
                onDismiss = { showEditDialog = null },
                onSave = { updatedFood ->
                    viewModel.updateFood(updatedFood)
                    showEditDialog = null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFoodDialog(
    food: FoodEntity, 
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit, 
    onSave: (FoodEntity) -> Unit
) {
    var name by remember { mutableStateOf(food.name) }
    var price by remember { mutableStateOf(food.price.toString()) }
    var desc by remember { mutableStateOf(food.description) }
    var imageUrl by remember { mutableStateOf(food.imageUrl) }
    var selectedCategoryId by remember { mutableStateOf(food.categoryId) }
    
    var categoryExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
                OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("Image URL") })
                
                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = categories.find { it.id == selectedCategoryId }?.name ?: "Select Category",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategoryId = category.id
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, minLines = 3)
            }
        },
        confirmButton = {
            TextButton(onClick = { 
                onSave(food.copy(
                    name = name, 
                    price = price.toDoubleOrNull() ?: food.price, 
                    description = desc,
                    imageUrl = imageUrl,
                    categoryId = selectedCategoryId
                ))
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun DashboardSmallCard(title: String, icon: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, style = MaterialTheme.typography.headlineMedium)
            Text(title, style = MaterialTheme.typography.labelMedium)
        }
    }
}

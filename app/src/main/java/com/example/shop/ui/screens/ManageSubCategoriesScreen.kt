package com.example.shop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.data.local.entity.CategoryEntity
import com.example.shop.viewmodel.RestaurantViewModel
import com.example.shop.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageSubCategoriesScreen(
    viewModel: RestaurantViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val categories by viewModel.categories.collectAsState()
    val subCategories by viewModel.subCategories.collectAsState()
    
    var selectedCategory by remember { mutableStateOf<CategoryEntity?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newSubName by remember { mutableStateOf("") }
    var categoryExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(selectedCategory) {
        selectedCategory?.let { viewModel.loadSubCategories(it.id) }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Manage Sub-Categories") }) },
        floatingActionButton = {
            if (selectedCategory != null) {
                FloatingActionButton(onClick = { showAddDialog = true }) {
                    Text("+")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("Select Parent Category", style = MaterialTheme.typography.titleMedium)
            
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = selectedCategory?.name ?: "Select a category",
                    onValueChange = {},
                    readOnly = true,
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
                                selectedCategory = category
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            if (selectedCategory == null) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Please select a category first")
                }
            } else {
                Text("Sub-Categories for ${selectedCategory?.name}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 16.dp)) {
                    if (subCategories.isEmpty()) {
                        item { Text("No sub-categories added yet.", style = MaterialTheme.typography.bodyMedium) }
                    }
                    items(subCategories) { sub ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(sub.name, modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add Sub-Category") },
                text = {
                    OutlinedTextField(
                        value = newSubName,
                        onValueChange = { newSubName = it },
                        label = { Text("Sub-Category Name") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newSubName.isNotBlank() && selectedCategory != null) {
                            viewModel.addSubCategory(selectedCategory!!.id, newSubName)
                            newSubName = ""
                            showAddDialog = false
                        }
                    }) { Text("Add") }
                }
            )
        }
    }
}

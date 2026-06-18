package com.example.shop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen() {

    val favorites = listOf(
        "Margherita Pizza",
        "Veg Burger",
        "Cold Coffee"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Favorites")
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {

            items(favorites) { food ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {

                    Text(
                        text = food,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
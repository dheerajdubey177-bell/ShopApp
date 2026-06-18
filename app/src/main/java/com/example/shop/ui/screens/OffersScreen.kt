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
fun OffersScreen() {

    val offers = listOf(
        "20% OFF On Pizza",
        "Buy 1 Get 1 Burger",
        "Free Drink Above ₹500"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Offers")
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {

            items(offers) { offer ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {

                    Text(
                        text = offer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
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
fun AddressScreen() {

    val addresses = listOf(
        "Home - Pune",
        "Office - Mumbai"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("My Addresses")
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {

            items(addresses) { address ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {

                    Text(
                        address,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
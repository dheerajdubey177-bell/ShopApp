package com.example.shop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageOrdersScreen() {

    val orders = listOf(
        "Order #1001 - Preparing",
        "Order #1002 - Delivered",
        "Order #1003 - Pending"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Manage Orders")
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {

            items(orders) { order ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(order)

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Button(
                            onClick = { }
                        ) {
                            Text("Update Status")
                        }
                    }
                }
            }
        }
    }
}
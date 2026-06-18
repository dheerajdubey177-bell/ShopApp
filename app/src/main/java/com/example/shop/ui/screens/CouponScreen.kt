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
fun CouponScreen() {

    val coupons = listOf(
        "SAVE20",
        "WELCOME50",
        "PIZZA10"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Coupons")
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {

            items(coupons) { coupon ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(coupon)

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Button(
                            onClick = { }
                        ) {
                            Text("Apply")
                        }
                    }
                }
            }
        }
    }
}
package com.example.shop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shop.model.Review

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen() {

    val reviews = listOf(
        Review(
            "John",
            4.5f,
            "Excellent Pizza"
        ),
        Review(
            "Sarah",
            5f,
            "Loved it"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Reviews")
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {

            items(reviews) { review ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(review.userName)

                        Text("⭐ ${review.rating}")

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(review.comment)
                    }
                }
            }
        }
    }
}
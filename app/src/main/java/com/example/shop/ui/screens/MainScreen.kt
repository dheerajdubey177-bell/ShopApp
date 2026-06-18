package com.example.shop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.shop.navigation.BottomNavItem
import com.example.shop.data.repository.SelectedRestaurantManager

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAddresses: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToCoupons: () -> Unit,
    onNavigateToReviews: () -> Unit
) {

    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem("home", "Home"),
        BottomNavItem("restaurants", "Explore"),
        BottomNavItem("menu", "Menu"),
        BottomNavItem("cart", "Cart"),
        BottomNavItem("profile", "Profile")
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute?.startsWith(item.route) == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Text(
                                when(item.route) {
                                    "home" -> "🏠"
                                    "restaurants" -> "🏢"
                                    "menu" -> "🍕"
                                    "cart" -> "🛒"
                                    else -> "👤"
                                }
                            )
                        },
                        label = {
                            Text(item.title)
                        }
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {

            composable("home") {
                HomeScreen(onRestaurantClick = { restaurantId ->
                    // Set global state
                    SelectedRestaurantManager.selectedRestaurantId.value = restaurantId
                    // Navigate to menu tab
                    navController.navigate("menu") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }

            composable("restaurants") {
                RestaurantsScreen(onRestaurantClick = { restaurantId ->
                    // Set global state
                    SelectedRestaurantManager.selectedRestaurantId.value = restaurantId
                    // Navigate to menu tab
                    navController.navigate("menu") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }

            composable("menu") {
                val restaurantId = SelectedRestaurantManager.selectedRestaurantId.value
                if (restaurantId != null) {
                    MenuScreen(restaurantId = restaurantId)
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Please select a restaurant from Home")
                    }
                }
            }

            composable("cart") {
                CartScreen(onCheckout = onNavigateToCheckout)
            }

            composable("profile") {
                ProfileScreen(
                    onLogout = onLogout,
                    onSettingsClick = onNavigateToSettings,
                    onAddressesClick = onNavigateToAddresses,
                    onFavoritesClick = onNavigateToFavorites,
                    onCouponsClick = onNavigateToCoupons,
                    onReviewsClick = onNavigateToReviews
                )
            }
        }
    }
}

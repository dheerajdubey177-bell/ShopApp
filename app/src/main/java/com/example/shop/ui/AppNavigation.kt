package com.example.shop.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shop.data.local.SessionManager
import com.example.shop.navigation.Routes
import com.example.shop.ui.screens.*

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        composable(Routes.SPLASH) {
            SplashScreen { isLoggedIn, role ->
                if (isLoggedIn) {
                    when (role) {
                        "ADMIN" -> {
                            navController.navigate(Routes.ADMIN_HOME) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                        }
                        "RESTAURANT_OWNER" -> {
                            navController.navigate("restaurant_dashboard") {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                        }
                        else -> {
                            navController.navigate(Routes.MAIN) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                        }
                    }
                } else {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            }
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onRegisterClick = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = { role ->
                    when (role) {
                        "ADMIN" -> {
                            navController.navigate(Routes.ADMIN_HOME) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        }
                        "RESTAURANT_OWNER" -> {
                            navController.navigate("restaurant_dashboard") {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        }
                        else -> {
                            navController.navigate(Routes.MAIN) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(onBackToLogin = { navController.popBackStack() })
        }

        composable(Routes.MAIN) {
            MainScreen(
                onLogout = {
                    sessionManager.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                },
                onNavigateToCheckout = { navController.navigate(Routes.CHECKOUT) },
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) },
                onNavigateToAddresses = { navController.navigate(Routes.ADDRESSES) },
                onNavigateToFavorites = { navController.navigate(Routes.FAVORITES) },
                onNavigateToCoupons = { navController.navigate(Routes.COUPONS) },
                onNavigateToReviews = { navController.navigate(Routes.REVIEWS) }
            )
        }

        composable("restaurant_dashboard") {
            RestaurantDashboardScreen(
                onLogout = {
                    sessionManager.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo("restaurant_dashboard") { inclusive = true }
                    }
                },
                onManageCategories = { navController.navigate("manage_categories") },
                onManageSubCategories = { /* TODO */ },
                onManageFoodItems = { navController.navigate(Routes.ADD_FOOD) }
            )
        }

        composable("manage_categories") {
            ManageCategoriesScreen()
        }

        composable(Routes.ADMIN_HOME) {
            AdminHomeScreen(
                onManageFoodClick = { navController.navigate(Routes.ADD_FOOD) },
                onManageOrdersClick = { navController.navigate(Routes.MANAGE_ORDERS) },
                onViewReportsClick = { /* TODO */ },
                onLogout = {
                    sessionManager.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ADMIN_HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ADD_FOOD) {
            AddFoodScreen(onFoodAdded = { navController.popBackStack() })
        }

        composable(Routes.MANAGE_ORDERS) {
            ManageOrdersScreen()
        }

        composable(Routes.CHECKOUT) {
            CheckoutScreen()
        }

        composable(Routes.SETTINGS) {
            SettingsScreen()
        }

        composable(Routes.ADDRESSES) {
            AddressScreen()
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen()
        }

        composable(Routes.COUPONS) {
            CouponScreen()
        }

        composable(Routes.REVIEWS) {
            ReviewsScreen()
        }
        
        composable(Routes.FOOD_DETAILS) {
            FoodDetailsScreen()
        }
    }
}
package com.example.shop.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.data.local.LocationData
import com.example.shop.model.UserType
import com.example.shop.ui.components.PrimaryButton
import com.example.shop.viewmodel.AuthUiEvent
import com.example.shop.viewmodel.AuthViewModel
import com.example.shop.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {

    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserType.CUSTOMER) }

    // Restaurant details
    var restaurantName by remember { mutableStateOf("") }
    var restaurantDesc by remember { mutableStateOf("") }
    var restaurantAddress by remember { mutableStateOf("") }
    
    var selectedState by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    
    var stateExpanded by remember { mutableStateOf(false) }
    var cityExpanded by remember { mutableStateOf(false) }

    var cuisineType by remember { mutableStateOf("Indian") }
    var logoUrl by remember { mutableStateOf("") }
    var bannerUrl by remember { mutableStateOf("") }

    val states = LocationData.getStates()
    val cities = if (selectedState.isNotBlank()) LocationData.getCities(selectedState) else emptyList()
    val cuisinesList = listOf("Indian", "Pizza", "Burger", "Coffee", "Chinese", "Fast Food")

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AuthUiEvent.Success -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    onBackToLogin()
                }
                is AuthUiEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Register As", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { selectedRole = UserType.CUSTOMER },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedRole == UserType.CUSTOMER) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (selectedRole == UserType.CUSTOMER) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("Customer")
            }

            Button(
                onClick = { selectedRole = UserType.RESTAURANT_OWNER },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedRole == UserType.RESTAURANT_OWNER) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (selectedRole == UserType.RESTAURANT_OWNER) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("Restaurant")
            }
        }

        if (selectedRole == UserType.RESTAURANT_OWNER) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Restaurant Details", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = restaurantName,
                onValueChange = { restaurantName = it },
                label = { Text("Restaurant Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = restaurantDesc,
                onValueChange = { restaurantDesc = it },
                label = { Text("Restaurant Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = restaurantAddress,
                onValueChange = { restaurantAddress = it },
                label = { Text("Exact Address") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            // State Selection
            ExposedDropdownMenuBox(
                expanded = stateExpanded,
                onExpandedChange = { stateExpanded = !stateExpanded }
            ) {
                OutlinedTextField(
                    value = if (selectedState.isBlank()) "Select State" else selectedState,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("State") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = stateExpanded,
                    onDismissRequest = { stateExpanded = false }
                ) {
                    states.forEach { state ->
                        DropdownMenuItem(
                            text = { Text(state) },
                            onClick = {
                                selectedState = state
                                selectedCity = "" // Reset city
                                stateExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // City Selection
            ExposedDropdownMenuBox(
                expanded = cityExpanded,
                onExpandedChange = { cityExpanded = !cityExpanded }
            ) {
                OutlinedTextField(
                    value = if (selectedCity.isBlank()) "Select City" else selectedCity,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("City") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = cityExpanded,
                    onDismissRequest = { cityExpanded = false }
                ) {
                    cities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = {
                                selectedCity = city
                                cityExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Select Cuisine", style = MaterialTheme.typography.labelMedium)
            Column {
                cuisinesList.chunked(3).forEach { chunk ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        chunk.forEach { c ->
                            FilterChip(
                                selected = cuisineType == c,
                                onClick = { cuisineType = c },
                                label = { Text(c) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = logoUrl,
                onValueChange = { logoUrl = it },
                label = { Text("Logo URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = bannerUrl,
                onValueChange = { bannerUrl = it },
                label = { Text("Banner URL") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Register",
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                when {
                    fullName.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                        Toast.makeText(context, "Please fill all user fields", Toast.LENGTH_SHORT).show()
                    }
                    password != confirmPassword -> {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                    selectedRole == UserType.RESTAURANT_OWNER && (restaurantName.isBlank() || selectedCity.isBlank()) -> {
                        Toast.makeText(context, "Please fill restaurant details and city", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        viewModel.register(
                            fullName, email, phone, password, selectedRole.name,
                            restaurantName, restaurantDesc, restaurantAddress,
                            cuisineType, selectedCity, logoUrl, bannerUrl
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = onBackToLogin
        ) {
            Text("Already have an account? Login")
        }
    }
}

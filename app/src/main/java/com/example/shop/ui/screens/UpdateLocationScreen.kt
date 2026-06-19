package com.example.shop.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shop.ui.components.MapPickerComponent
import com.example.shop.viewmodel.ProfileViewModel
import com.example.shop.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun UpdateLocationScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    MapPickerComponent(
        onLocationSelected = { latLng ->
            scope.launch {
                viewModel.updateUserLocation(latLng.latitude, latLng.longitude, "Manual Pin")
                Toast.makeText(context, "Location Updated Successfully!", Toast.LENGTH_SHORT).show()
                onBack()
            }
        }
    )
}

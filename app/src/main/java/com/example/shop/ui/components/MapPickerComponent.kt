package com.example.shop.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapPickerComponent(
    onLocationSelected: (LatLng) -> Unit,
    onDismiss: () -> Unit
) {
    val initialLocation = LatLng(19.0760, 72.8777) // Mumbai as default
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 12f)
    }
    
    var markerPosition by remember { mutableStateOf(initialLocation) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                markerPosition = it
            }
        ) {
            Marker(
                state = MarkerState(position = markerPosition),
                title = "Delivery Location",
                draggable = true
            )
        }

        Button(
            onClick = { onLocationSelected(markerPosition) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Text("Confirm Location")
        }
    }
}

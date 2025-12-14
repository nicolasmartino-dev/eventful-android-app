package com.eventful.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.eventful.app.R

/**
 * A simple map view without card wrapper, with disabled panning
 */
@Composable
fun SimpleMapView(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier,
    height: Int = 400 // Double the default height
) {
    val context = LocalContext.current
    val eventLocation = LatLng(latitude, longitude)
    val cameraPosition = remember {
        CameraPosition.Builder()
            .target(eventLocation)
            .zoom(15f)
            .build()
    }
    val cameraPositionState = remember { CameraPositionState(cameraPosition) }
    val markerState = remember { MarkerState(position = eventLocation) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Map without card wrapper
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                android.util.Log.d("SimpleMapView", "✅ Simple map view loaded successfully!")
            },
            // Disable panning but allow zoom controls
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                scrollGesturesEnabled = false,
                zoomGesturesEnabled = false,
                tiltGesturesEnabled = false
            )
        ) {
            Marker(
                state = markerState,
                title = context.getString(R.string.event_location)
            )
        }

        // Clickable text to open in Google Maps
        Text(
            text = context.getString(R.string.tap_to_open_in_google_maps),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    openInGoogleMaps(context, latitude, longitude)
                }
                .padding(vertical = 8.dp)
        )
    }
}

private fun openInGoogleMaps(context: android.content.Context, latitude: Double, longitude: Double) {
    try {
        val mapUri = "geo:$latitude,$longitude".toUri()
        val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            // Fallback to generic geo intent
            val genericMapIntent = Intent(Intent.ACTION_VIEW, mapUri)
            context.startActivity(genericMapIntent)
        }
    } catch (e: Exception) {
        // Final fallback to browser
        try {
            val webUri = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude".toUri()
            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
            context.startActivity(webIntent)
        } catch (webException: Exception) {
            android.util.Log.e("SimpleMapView", "Failed to open maps: ${e.message}")
        }
    }
}

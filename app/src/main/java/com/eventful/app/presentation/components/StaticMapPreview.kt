package com.eventful.app.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import android.content.Intent
import android.net.Uri

/**
 * A composable that displays a static map image as a fallback when the interactive map fails
 */
@Composable
fun StaticMapPreview(
    latitude: Double,
    longitude: Double,
    locationName: String? = null,
    modifier: Modifier = Modifier,
    height: Int = 200
) {
    val context = LocalContext.current
    
    // Create static map URL
    val staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=$latitude,$longitude&zoom=15&size=400x${height}&markers=color:red%7C$latitude,$longitude&key=AIzaSyDxsJBc9v5bJYVUHIDCbY3y-RzAPFk-Qlo"
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📍 Static Map",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                if (locationName != null) {
                    Text(
                        text = locationName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }

            // Static map image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .clickable {
                        // Open in Google Maps when clicked
                        try {
                            val mapUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
                            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            
                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(mapIntent)
                            } else {
                                // Fallback to browser
                                val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
                                val webIntent = Intent(Intent.ACTION_VIEW, webUri)
                                context.startActivity(webIntent)
                            }
                        } catch (e: Exception) {
                            android.util.Log.e("StaticMapPreview", "Failed to open maps: ${e.message}")
                        }
                    }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(staticMapUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Static map of event location",
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Footer with coordinates
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${String.format("%.4f", latitude)}, ${String.format("%.4f", longitude)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Tap to open in Google Maps",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


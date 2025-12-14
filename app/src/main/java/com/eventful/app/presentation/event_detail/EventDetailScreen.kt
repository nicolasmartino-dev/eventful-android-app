package com.eventful.app.presentation.event_detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.eventful.app.domain.model.Event
import com.eventful.app.presentation.components.*
import com.eventful.app.R
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    navController: NavController,
    viewModel: EventDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when {
            state.isLoading -> {
                LoadingSpinnerWithText(
                    text = "Loading event details...",
                    size = SpinnerSize.LARGE,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            state.error.isNotBlank() -> {
                NetworkError(
                    onRetry = { /* Retry logic */ },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            state.event != null -> {
                EnhancedEventDetailContent(
                    event = state.event!!,
                    onBackClick = { navController.popBackStack() },
                    context = context
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedEventDetailContent(
    event: Event,
    onBackClick: () -> Unit,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Image with overlay and back button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = event.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Dark gradient overlay at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )
            
            // Title on overlay
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            )
            
            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
        
        // Event Details Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Date & Time Section
            DetailSection(
                icon = Icons.Default.CalendarToday,
                iconColor = Color(0xFF007AFF), // Blue
                title = "Date & Time",
                content = event.startTime?.let { formatDateTime(it) } ?: "Date TBD"
            )
            
            // Add to Calendar Button
            AddToCalendarButton(
                onClick = { /* TODO: Add to calendar */ }
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))
            
            // Location Section
            DetailSection(
                icon = Icons.Default.LocationOn,
                iconColor = Color(0xFF34C759), // Green
                title = "Location",
                content = buildLocationInfo(event).displayText
            )
            
            // Map View
            val locationInfo = buildLocationInfo(event)
            if (locationInfo.hasCoordinates) {
                Spacer(modifier = Modifier.height(12.dp))
                SimpleMapView(
                    latitude = locationInfo.latitude!!,
                    longitude = locationInfo.longitude!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))
            
            // Price Section
            DetailSection(
                icon = Icons.Default.AttachMoney,
                iconColor = Color(0xFF34C759), // Green
                title = "Price",
                content = when {
                    event.isFree == true -> "Free"
                    event.priceInfo != null && event.priceInfo != "nan" -> event.priceInfo!!
                    else -> "Price TBD"
                }
            )
            
            // Description Section
            event.description?.let { desc ->
                if (desc != "nan" && desc.isNotBlank()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    DetailSection(
                        icon = Icons.Default.Description,
                        iconColor = Color(0xFF007AFF), // Blue
                        title = "Description",
                        content = desc
                    )
                }
            }
            
            // Organizer Section
            event.organizer?.let { organizer ->
                if (organizer != "nan" && organizer.isNotBlank()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    DetailSection(
                        icon = Icons.Default.Person,
                        iconColor = Color(0xFF007AFF), // Blue
                        title = "Organizer",
                        content = organizer
                    )
                }
            }
            
            // Contact Info Section
            event.contactInfo?.let { contact ->
                if (contact != "nan" && contact.isNotBlank()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    DetailSection(
                        icon = Icons.Default.Phone,
                        iconColor = Color(0xFF34C759), // Green
                        title = "Contact",
                        content = contact
                    )
                }
            }
            
            // View Website Button
            event.eventUrl?.let { eventUrl ->
                if (eventUrl != "nan" && eventUrl.isNotBlank()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = { openEventUrl(context, eventUrl) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF007AFF)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "View Website",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
                }
        }
    }


@Composable
fun DetailSection(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Title with Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Content
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}

private fun formatDateTime(dateTimeString: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(dateTimeString)
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a", Locale.ENGLISH)
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        dateTimeString // Fallback to original string if parsing fails
    }
}

private fun openGoogleMaps(context: Context, address: String) {
    try {
        // Create a URI for the address
        val encodedAddress = Uri.encode(address)
        val mapUri = "geo:0,0?q=$encodedAddress".toUri()
        
        // Create intent to open Google Maps
        val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        
        // Check if Google Maps is available, otherwise use generic geo intent
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            // Fallback to generic geo intent
            val genericMapIntent = Intent(Intent.ACTION_VIEW, mapUri)
            context.startActivity(genericMapIntent)
        }
    } catch (e: Exception) {
        // If all else fails, try opening in browser
        try {
            val webUri = "https://www.google.com/maps/search/?api=1&query=${Uri.encode(address)}".toUri()
            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
            context.startActivity(webIntent)
        } catch (webException: Exception) {
            // Log error or show user-friendly message
            android.util.Log.e("EventDetailScreen", "Failed to open maps: ${e.message}")
        }
    }
}

private fun openGoogleMapsWithCoordinates(context: Context, latitude: Double, longitude: Double, address: String? = null) {
    try {
        // Create a URI with exact coordinates
        val mapUri = if (address != null) {
            val encodedAddress = Uri.encode(address)
            "geo:$latitude,$longitude?q=$latitude,$longitude($encodedAddress)".toUri()
        } else {
            "geo:$latitude,$longitude".toUri()
        }
        
        // Create intent to open Google Maps
        val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        
        // Check if Google Maps is available, otherwise use generic geo intent
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            // Fallback to generic geo intent
            val genericMapIntent = Intent(Intent.ACTION_VIEW, mapUri)
            context.startActivity(genericMapIntent)
        }
    } catch (e: Exception) {
        // If all else fails, try opening in browser
        try {
            val webUri = if (address != null) {
                val encodedAddress = Uri.encode(address)
                "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude".toUri()
            } else {
                "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude".toUri()
            }
            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
            context.startActivity(webIntent)
        } catch (webException: Exception) {
            android.util.Log.e("EventDetailScreen", "Failed to open maps with coordinates: ${e.message}")
        }
    }
}


/**
 * Data class to hold location information for display
 */
private data class LocationInfo(
    val displayText: String,
    val hasCoordinates: Boolean,
    val latitude: Double? = null,
    val longitude: Double? = null
)

/**
 * Builds location information with smart priority order based on available data
 */
private fun buildLocationInfo(event: Event): LocationInfo {
    val coordinates = event.geom?.let { Pair(it.latitude, it.longitude) }
    val hasValidCoordinates = coordinates != null && coordinates.first != null && coordinates.second != null
    
    // Priority 1: Specific location name (venue name)
    event.locationName?.let { locationName ->
        if (locationName != "nan" && locationName.isNotBlank()) {
            return LocationInfo(
                displayText = locationName,
                hasCoordinates = hasValidCoordinates,
                latitude = coordinates?.first,
                longitude = coordinates?.second
            )
        }
    }
    
    // Priority 2: Specific address
    event.address?.let { address ->
        if (address != "nan" && address.isNotBlank()) {
            val addressText = buildAddressString(event, address)
            return LocationInfo(
                displayText = addressText,
                hasCoordinates = hasValidCoordinates,
                latitude = coordinates?.first,
                longitude = coordinates?.second
            )
        }
    }
    
    // Priority 3: City/State combination
    val cityState = buildCityStateString(event)
    if (cityState.isNotBlank()) {
        return LocationInfo(
            displayText = cityState,
            hasCoordinates = hasValidCoordinates,
            latitude = coordinates?.first,
            longitude = coordinates?.second
        )
    }
    
    // Priority 4: Just coordinates if available - show clean "Location" text
    if (hasValidCoordinates) {
        return LocationInfo(
            displayText = "Location",
            hasCoordinates = true,
            latitude = coordinates!!.first!!,
            longitude = coordinates.second!!
        )
    }
    
    // Fallback: No location information
    return LocationInfo(
        displayText = "Location not specified", // This will be handled by the calling context
        hasCoordinates = false
    )
}

/**
 * Builds a comprehensive address string from event data
 */
private fun buildAddressString(event: Event, primaryAddress: String): String {
    val addressParts = mutableListOf<String>()
    
    // Add the primary address
    addressParts.add(primaryAddress)
    
    // Add city and state/province
    val cityState = buildCityStateString(event)
    if (cityState.isNotBlank()) {
        addressParts.add(cityState)
    }
    
    // Add zip code if available
    event.zipCode?.let { zipCode ->
        if (zipCode != "nan" && zipCode.isNotBlank()) {
            addressParts.add(zipCode)
        }
    }
    
    // Add country if it's not Canada (assuming most events are in Canada)
    event.country?.let { country ->
        if (country != "nan" && country.isNotBlank() && 
            !country.equals("Canada", ignoreCase = true) && 
            !country.equals("CA", ignoreCase = true)) {
            addressParts.add(country)
        }
    }
    
    return addressParts.joinToString(", ")
}

/**
 * Builds city and state/province string
 */
private fun buildCityStateString(event: Event): String {
    val cityStateParts = mutableListOf<String>()
    
    // Add city
    event.city?.let { city ->
        if (city != "nan" && city.isNotBlank()) {
            cityStateParts.add(city)
        }
    }
    
    // Add state/province
    event.stateProvince?.let { stateProvince ->
        if (stateProvince != "nan" && stateProvince.isNotBlank()) {
            cityStateParts.add(stateProvince)
        }
    }
    
    return cityStateParts.joinToString(", ")
}

private fun openEventUrl(context: Context, eventUrl: String) {
    try {
        val uri = eventUrl.toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        
        // Check if there's an app that can handle this URL
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            // Fallback: try to open in browser
            val webIntent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(webIntent)
        }
    } catch (e: Exception) {
        android.util.Log.e("EventDetailScreen", "Failed to open event URL: ${e.message}")
    }
}

package com.eventful.app.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun NoEventsFound(
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyStateTemplate(
        icon = Icons.Default.CalendarToday,
        iconTint = Color(0xFF007AFF),
        title = "No Events Found",
        message = "Try adjusting your filters or check back later",
        actionText = "Clear Filters",
        onAction = onClearFilters,
        modifier = modifier
    )
}

@Composable
fun NetworkError(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyStateTemplate(
        icon = Icons.Default.WifiOff,
        iconTint = Color(0xFFFF3B30), // Red
        title = "Connection Lost",
        message = "Check your internet and try again",
        actionText = "Retry",
        onAction = onRetry,
        modifier = modifier
    )
}

@Composable
private fun EmptyStateTemplate(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    message: String,
    actionText: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(80.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onAction
        ) {
            Text(actionText)
        }
    }
}



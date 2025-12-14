package com.eventful.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PriceBadge(
    isFree: Boolean?,
    priceInfo: String?,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, displayText) = when {
        isFree == true -> Triple(
            Color(0xFF34C759), // Green
            Color.White,
            "FREE"
        )
        priceInfo != null -> Triple(
            Color(0xFF8E8E93), // Gray
            Color.White,
            priceInfo
        )
        else -> return // Don't show badge if no price info
    }
    
    Text(
        text = displayText,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun SoldOutBadge(modifier: Modifier = Modifier) {
    Text(
        text = "SOLD OUT",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = modifier
            .background(Color(0xFFFF3B30), RoundedCornerShape(4.dp)) // Red
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun FeaturedBadge(modifier: Modifier = Modifier) {
    Text(
        text = "Featured",
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        modifier = modifier
            .background(Color(0xFFFF9500), RoundedCornerShape(4.dp)) // Orange
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
fun EndingSoonBadge(modifier: Modifier = Modifier) {
    Text(
        text = "Ending Soon",
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        modifier = modifier
            .background(Color(0xFFFF3B30), RoundedCornerShape(4.dp)) // Red
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

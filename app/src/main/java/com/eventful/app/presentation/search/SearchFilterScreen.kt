package com.eventful.app.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterOverlay(
    onDismiss: () -> Unit,
    onApplyFilters: (SearchFilterState) -> Unit,
    viewModel: SearchFilterViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Top Bar with Close Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            // Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                // Search Bar
                item {
                    SearchBar(
                        query = state.searchQuery,
                        onQueryChange = { viewModel.onSearchQueryChange(it) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                // Categories Section
                item {
                    FilterSection(title = "Categories") {
                        CategoryChips(
                            selectedCategories = state.selectedCategories,
                            onCategoryToggle = { viewModel.onCategoryToggle(it) }
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                // Date Section
                item {
                    FilterSection(title = "Date") {
                        DateFilterChips(
                            selectedDate = state.selectedDateFilter,
                            onDateSelect = { viewModel.onDateFilterChange(it) }
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                // Price Section
                item {
                    FilterSection(title = "Price") {
                        PriceFilterChips(
                            selectedPrice = state.selectedPriceFilter,
                            onPriceSelect = { viewModel.onPriceFilterChange(it) }
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                // Location Section
                item {
                    FilterSection(title = "Location") {
                        LocationSlider(
                            selectedRadius = state.selectedRadius,
                            onRadiusChange = { viewModel.onRadiusChange(it) }
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            
            // Bottom Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(20.dp)
            ) {
                // Clear All Button
                TextButton(
                    onClick = { viewModel.clearAllFilters() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Clear All",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Apply Filters Button
                Button(
                    onClick = {
                        onApplyFilters(state)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007AFF)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Apply Filters",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = {
            Text("Search events...")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF007AFF),
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
        )
    )
}

@Composable
fun FilterSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
fun CategoryChips(
    selectedCategories: Set<String>,
    onCategoryToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("Music", "Art & Culture", "Food & Drink", "Sports", "Nightlife", "Community")
    
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories.size) { index ->
            val category = categories[index]
            FilterChip(
                selected = selectedCategories.contains(category),
                onClick = { onCategoryToggle(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF007AFF),
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
fun DateFilterChips(
    selectedDate: DateFilter,
    onDateSelect: (DateFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFilters = listOf(
        DateFilter.TODAY,
        DateFilter.THIS_WEEK,
        DateFilter.THIS_MONTH,
        DateFilter.PICK_A_DATE
    )
    
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dateFilters.size) { index ->
            val filter = dateFilters[index]
            FilterChip(
                selected = selectedDate == filter,
                onClick = { onDateSelect(filter) },
                label = { Text(filter.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF007AFF),
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
fun PriceFilterChips(
    selectedPrice: PriceFilter,
    onPriceSelect: (PriceFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val priceFilters = listOf(
        PriceFilter.FREE,
        PriceFilter.UNDER_25,
        PriceFilter.UNDER_100,
        PriceFilter.ANY_PRICE
    )
    
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(priceFilters.size) { index ->
            val filter = priceFilters[index]
            FilterChip(
                selected = selectedPrice == filter,
                onClick = { onPriceSelect(filter) },
                label = { Text(filter.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF007AFF),
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
fun LocationSlider(
    selectedRadius: Int,
    onRadiusChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Within ${selectedRadius}km",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Slider(
            value = selectedRadius.toFloat(),
            onValueChange = { onRadiusChange(it.toInt()) },
            valueRange = 5f..50f,
            steps = 3, // 5, 10, 25, 50
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF007AFF),
                activeTrackColor = Color(0xFF007AFF),
                inactiveTrackColor = Color(0xFF007AFF).copy(alpha = 0.3f)
            )
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("5km", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Text("10km", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Text("25km", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Text("50km", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}

// Enums for filter types
enum class DateFilter(val displayName: String) {
    TODAY("Today"),
    THIS_WEEK("This Week"),
    THIS_MONTH("This Month"),
    PICK_A_DATE("Pick a Date")
}

enum class PriceFilter(val displayName: String) {
    FREE("Free"),
    UNDER_25("Under $25"),
    UNDER_100("Under $100"),
    ANY_PRICE("Any Price")
}







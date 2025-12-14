package com.eventful.app.presentation.event_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.eventful.app.domain.model.Event
import com.eventful.app.presentation.components.*
import com.eventful.app.presentation.search.SearchFilterOverlay
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    navController: NavController,
    viewModel: EventListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val listState = rememberLazyListState()
    var showFilterOverlay by remember { mutableStateOf(false) }

    // Detect when user is near the end of the list
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            
            lastVisibleItemIndex >= totalItemsNumber - 3 && 
            state.hasMoreEvents && 
            !state.isLoadingMore
        }
    }

    // Load more events when user approaches the end
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadMoreEvents()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Events in Montreal",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { showFilterOverlay = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filters",
                            tint = Color(0xFF007AFF)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
        when {
            state.isLoading && state.events.isEmpty() -> {
                LoadingSpinnerWithText(
                    text = "Loading events...",
                    size = SpinnerSize.LARGE,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            state.error.isNotBlank() && state.events.isEmpty() -> {
                NetworkError(
                    onRetry = { viewModel.loadMoreEvents() },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            state.events.isEmpty() -> {
                NoEventsFound(
                    onClearFilters = { /* TODO: Clear filters */ },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(state.events) { event ->
                        EnhancedEventListItem(event = event) {
                            navController.navigate("event_detail_screen/${event.id}")
                        }
                    }
                    
                    // Show loading indicator at the bottom when loading more
                    if (state.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CustomLoadingSpinner(size = SpinnerSize.MEDIUM)
                            }
                        }
                    }
                }
            }
        }
        
        // Show Search & Filter Overlay
        if (showFilterOverlay) {
            SearchFilterOverlay(
                onDismiss = { showFilterOverlay = false },
                onApplyFilters = { filters ->
                    // TODO: Apply filters to event list
                    // viewModel.applyFilters(filters)
                }
            )
        }
    }
    }
}

@Composable
fun EnhancedEventListItem(
    event: com.eventful.app.domain.model.Event,
    onItemClick: (com.eventful.app.domain.model.Event) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(event) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Hero Image with Badges
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = event.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )
                
                // Price Badge in top-right corner
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    PriceBadge(
                        isFree = event.isFree,
                        priceInfo = event.priceInfo
                    )
                }
            }
            
            // Event Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Date with icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Date",
                            tint = Color(0xFF007AFF), // Blue
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = event.startTime?.let { formatShortDate(it) } ?: "Date TBD",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF007AFF)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Location with icon
                    event.locationName?.let { location ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = Color(0xFF34C759), // Green
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = location,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF34C759),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatShortDate(dateTimeString: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(dateTimeString)
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        dateTimeString // Fallback to original string if parsing fails
    }
}

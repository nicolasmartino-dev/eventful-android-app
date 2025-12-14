package com.eventful.app.presentation.event_list

import com.eventful.app.domain.model.Event

data class EventListState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val error: String = "",
    val isLoadingMore: Boolean = false,
    val hasMoreEvents: Boolean = true,
    val currentOffset: Int = 0
)

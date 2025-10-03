package com.example.eventful.presentation.event_list

import com.example.eventful.domain.model.Event

data class EventListState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val error: String = ""
)

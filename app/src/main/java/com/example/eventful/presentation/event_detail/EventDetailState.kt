package com.example.eventful.presentation.event_detail

import com.example.eventful.domain.model.Event

data class EventDetailState(
    val isLoading: Boolean = false,
    val event: Event? = null,
    val error: String = ""
)

package com.eventful.app.presentation.event_detail

import com.eventful.app.domain.model.Event

data class EventDetailState(
    val isLoading: Boolean = false,
    val event: Event? = null,
    val error: String = ""
)

package com.example.eventful.domain.model

import java.util.Date

data class Event(
    val id: String,
    val sourceId: String?,
    val sourceName: String?,
    val title: String,
    val description: String?,
    val startTime: Date,
    val endTime: Date?,
    val locationName: String?,
    val address: String?,
    val city: String?,
    val stateProvince: String?,
    val zipCode: String?,
    val country: String?,
    val geom: String?, // Representing GEOGRAPHY(Point, 4326) as WKT string
    val organizer: String?,
    val contactInfo: String?,
    val eventUrl: String?,
    val imageUrl: String?,
    val category: String?,
    val isFree: Boolean?,
    val priceInfo: String?,
    val status: String?,
    val createdAt: Date?,
    val updatedAt: Date?
)

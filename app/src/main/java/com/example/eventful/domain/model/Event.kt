package com.example.eventful.domain.model

data class Event(
    val id: String,
    val sourceId: String?,
    val sourceName: String?,
    val title: String,
    val description: String?,
    val startTime: String?,
    val endTime: String?,
    val locationName: String?,
    val address: String?,
    val city: String?,
    val stateProvince: String?,
    val zipCode: String?,
    val country: String?,
    val geom: String?,
    val organizer: String?,
    val contactInfo: String?,
    val eventUrl: String?,
    val imageUrl: String?,
    val category: String?,
    val isFree: Boolean?,
    val priceInfo: String?,
    val status: String?,
    val createdAt: String?,
    val updatedAt: String?
)

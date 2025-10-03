package com.example.eventful.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.eventful.domain.model.Event
import java.util.Date

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
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
    val geom: String?,
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
) {
    fun toEvent(): Event {
        return Event(
            id = id,
            sourceId = sourceId,
            sourceName = sourceName,
            title = title,
            description = description,
            startTime = startTime,
            endTime = endTime,
            locationName = locationName,
            address = address,
            city = city,
            stateProvince = stateProvince,
            zipCode = zipCode,
            country = country,
            geom = geom,
            organizer = organizer,
            contactInfo = contactInfo,
            eventUrl = eventUrl,
            imageUrl = imageUrl,
            category = category,
            isFree = isFree,
            priceInfo = priceInfo,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

fun Event.toEventEntity(): EventEntity {
    return EventEntity(
        id = id,
        sourceId = sourceId,
        sourceName = sourceName,
        title = title,
        description = description,
        startTime = startTime,
        endTime = endTime,
        locationName = locationName,
        address = address,
        city = city,
        stateProvince = stateProvince,
        zipCode = zipCode,
        country = country,
        geom = geom,
        organizer = organizer,
        contactInfo = contactInfo,
        eventUrl = eventUrl,
        imageUrl = imageUrl,
        category = category,
        isFree = isFree,
        priceInfo = priceInfo,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

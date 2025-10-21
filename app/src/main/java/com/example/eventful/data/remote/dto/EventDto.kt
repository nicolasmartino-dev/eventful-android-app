package com.example.eventful.data.remote.dto

import com.example.eventful.apollo.AllEventsQuery
import com.example.eventful.apollo.EventDetailsQuery
import com.example.eventful.domain.model.Event

import java.util.Date

// Extension function to convert Apollo Date to Java Date (if needed, Apollo usually handles it)
// For simplicity, we assume Apollo's generated types directly provide java.util.Date or can be easily converted.
// If Apollo uses custom scalars for Date/DateTime, a custom scalar adapter would be needed.
// For now, we'll assume direct conversion or that Apollo handles the parsing.

fun AllEventsQuery.AllEvent.toEvent(): Event {
    return Event(
        id = id,
        sourceId = sourceId as? String,
        sourceName = sourceName as? String,
        title = title,
        description = description as? String,
        startTime = startTime as? String, // Apollo returns DateTime as String
        endTime = endTime as? String,    // Apollo returns DateTime as String
        locationName = locationName as? String,
        address = address as? String,
        city = city as? String,
        stateProvince = stateProvince as? String,
        zipCode = zipCode as? String,
        country = country as? String,
        geom = geom as? String,
        organizer = organizer as? String,
        contactInfo = contactInfo as? String,
        eventUrl = eventUrl as? String,
        imageUrl = imageUrl as? String,
        category = category as? String,
        isFree = isFree as? Boolean,
        priceInfo = priceInfo as? String,
        status = status as? String,
        createdAt = createdAt as? String, // Apollo returns DateTime as String
        updatedAt = updatedAt as? String  // Apollo returns DateTime as String
    )
}

fun EventDetailsQuery.EventById.toEvent(): Event {
    return Event(
        id = id,
        sourceId = sourceId as? String,
        sourceName = sourceName as? String,
        title = title,
        description = description as? String,
        startTime = startTime as? String, // Apollo returns DateTime as String
        endTime = endTime as? String,    // Apollo returns DateTime as String
        locationName = locationName as? String,
        address = address as? String,
        city = city as? String,
        stateProvince = stateProvince as? String,
        zipCode = zipCode as? String,
        country = country as? String,
        geom = geom as? String,
        organizer = organizer as? String,
        contactInfo = contactInfo as? String,
        eventUrl = eventUrl as? String,
        imageUrl = imageUrl as? String,
        category = category as? String,
        isFree = isFree as? Boolean,
        priceInfo = priceInfo as? String,
        status = status as? String,
        createdAt = createdAt as? String, // Apollo returns DateTime as String
        updatedAt = updatedAt as? String  // Apollo returns DateTime as String
    )
}

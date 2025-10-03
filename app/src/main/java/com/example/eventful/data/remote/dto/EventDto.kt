package com.example.eventful.data.remote.dto

import com.example.eventful.domain.model.Event
import com.example.eventful.AllEventsQuery
import com.example.eventful.EventDetailsQuery
import java.util.Date

// Extension function to convert Apollo Date to Java Date (if needed, Apollo usually handles it)
// For simplicity, we assume Apollo's generated types directly provide java.util.Date or can be easily converted.
// If Apollo uses custom scalars for Date/DateTime, a custom scalar adapter would be needed.
// For now, we'll assume direct conversion or that Apollo handles the parsing.

fun AllEventsQuery.AllEvent.toEvent(): Event {
    return Event(
        id = id,
        sourceId = sourceId,
        sourceName = sourceName,
        title = title,
        description = description,
        startTime = startTime as Date, // Assuming Apollo maps GraphQL DateTime to Java Date
        endTime = endTime as Date?,    // Assuming Apollo maps GraphQL DateTime to Java Date
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
        createdAt = createdAt as Date?, // Assuming Apollo maps GraphQL DateTime to Java Date
        updatedAt = updatedAt as Date?  // Assuming Apollo maps GraphQL DateTime to Java Date
    )
}

fun EventDetailsQuery.EventById.toEvent(): Event {
    return Event(
        id = id,
        sourceId = sourceId,
        sourceName = sourceName,
        title = title,
        description = description,
        startTime = startTime as Date, // Assuming Apollo maps GraphQL DateTime to Java Date
        endTime = endTime as Date?,    // Assuming Apollo maps GraphQL DateTime to Java Date
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
        createdAt = createdAt as Date?, // Assuming Apollo maps GraphQL DateTime to Java Date
        updatedAt = updatedAt as Date?  // Assuming Apollo maps GraphQL DateTime to Java Date
    )
}

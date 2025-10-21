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
        description = (description as? String)?.takeIf { it != "nan" },
        startTime = startTime as? String, // Apollo returns DateTime as String
        endTime = endTime as? String,    // Apollo returns DateTime as String
        locationName = (locationName as? String)?.takeIf { it != "nan" },
        address = (address as? String)?.takeIf { it != "nan" },
        city = (city as? String)?.takeIf { it != "nan" },
        stateProvince = (stateProvince as? String)?.takeIf { it != "nan" },
        zipCode = (zipCode as? String)?.takeIf { it != "nan" },
        country = (country as? String)?.takeIf { it != "nan" },
        geom = geom as? String,
        organizer = (organizer as? String)?.takeIf { it != "nan" },
        contactInfo = (contactInfo as? String)?.takeIf { it != "nan" },
        eventUrl = (eventUrl as? String)?.takeIf { it != "nan" },
        imageUrl = (imageUrl as? String)?.takeIf { it != "nan" },
        category = (category as? String)?.takeIf { it != "nan" },
        isFree = isFree as? Boolean,
        priceInfo = (priceInfo as? String)?.takeIf { it != "nan" },
        status = (status as? String)?.takeIf { it != "nan" },
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
        description = (description as? String)?.takeIf { it != "nan" },
        startTime = startTime as? String, // Apollo returns DateTime as String
        endTime = endTime as? String,    // Apollo returns DateTime as String
        locationName = (locationName as? String)?.takeIf { it != "nan" },
        address = (address as? String)?.takeIf { it != "nan" },
        city = (city as? String)?.takeIf { it != "nan" },
        stateProvince = (stateProvince as? String)?.takeIf { it != "nan" },
        zipCode = (zipCode as? String)?.takeIf { it != "nan" },
        country = (country as? String)?.takeIf { it != "nan" },
        geom = geom as? String,
        organizer = (organizer as? String)?.takeIf { it != "nan" },
        contactInfo = (contactInfo as? String)?.takeIf { it != "nan" },
        eventUrl = (eventUrl as? String)?.takeIf { it != "nan" },
        imageUrl = (imageUrl as? String)?.takeIf { it != "nan" },
        category = (category as? String)?.takeIf { it != "nan" },
        isFree = isFree as? Boolean,
        priceInfo = (priceInfo as? String)?.takeIf { it != "nan" },
        status = (status as? String)?.takeIf { it != "nan" },
        createdAt = createdAt as? String, // Apollo returns DateTime as String
        updatedAt = updatedAt as? String  // Apollo returns DateTime as String
    )
}

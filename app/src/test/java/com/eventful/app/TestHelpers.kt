package com.eventful.app

import com.eventful.app.domain.model.Geometry
import com.eventful.app.domain.model.Event

object TestHelpers {
    
    fun createTestEvent(
        id: String = "test-id",
        title: String = "Test Event",
        startTime: String = "2024-01-01T10:00:00Z",
        description: String? = "Test Description",
        endTime: String? = null,
        locationName: String? = null,
        address: String? = null,
        city: String? = null,
        stateProvince: String? = null,
        zipCode: String? = null,
        country: String? = null,
        geom: Geometry? = null,
        organizer: String? = null,
        contactInfo: String? = null,
        eventUrl: String? = null,
        imageUrl: String? = null,
        category: String? = null,
        isFree: Boolean? = null,
        priceInfo: String? = null,
        status: String? = null,
        createdAt: String? = null,
        updatedAt: String? = null,
        sourceId: String? = null,
        sourceName: String? = null
    ): Event {
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



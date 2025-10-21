package com.example.eventful.data.mapper

import com.example.eventful.apollo.AllEventsQuery
import com.example.eventful.apollo.EventDetailsQuery
import com.example.eventful.data.local.EventEntity
import com.example.eventful.domain.model.Event

object EventMapper {

    fun AllEventsQuery.AllEvent.toEvent(): Event {
        return Event(
            id = id,
            sourceId = sourceId as? String,
            sourceName = sourceName as? String,
            title = title,
            description = (description as? String)?.takeIf { it != "nan" },
            startTime = startTime as? String,
            endTime = endTime as? String,
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
            createdAt = createdAt as? String,
            updatedAt = updatedAt as? String
        )
    }

    fun EventDetailsQuery.EventById.toEvent(): Event {
        return Event(
            id = id,
            sourceId = sourceId as? String,
            sourceName = sourceName as? String,
            title = title,
            description = (description as? String)?.takeIf { it != "nan" },
            startTime = startTime as? String,
            endTime = endTime as? String,
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
            createdAt = createdAt as? String,
            updatedAt = updatedAt as? String
        )
    }

    fun EventEntity.toEvent(): Event {
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
}

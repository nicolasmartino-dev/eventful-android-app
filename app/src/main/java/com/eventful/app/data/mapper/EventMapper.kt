package com.eventful.app.data.mapper

import com.eventful.app.apollo.AllEventsQuery
import com.eventful.app.apollo.EventDetailsQuery
import com.eventful.app.data.local.EventEntity
import com.eventful.app.domain.model.Event
import com.eventful.app.domain.model.Geometry

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
            geom = parseGeometry(geom),
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
            geom = parseGeometry(geom),
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
            geom = parseGeometry(geom),
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
            geom = geom?.let { "${it.type}:${it.coordinates.joinToString(",")}" },
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

    private fun parseGeometry(geom: Any?): Geometry? {
        return try {
            when (geom) {
                is Map<*, *> -> {
                    val type = geom["type"] as? String
                    val coordinates = geom["coordinates"] as? List<*>
                    if (type != null && coordinates != null) {
                        val doubleCoordinates = coordinates.mapNotNull { it as? Double }
                        if (doubleCoordinates.size >= 2) {
                            Geometry(type, doubleCoordinates)
                        } else null
                    } else null
                }
                is String -> {
                    // Handle our custom format: "Point:longitude,latitude"
                    if (geom.contains(":")) {
                        val parts = geom.split(":", limit = 2)
                        if (parts.size == 2) {
                            val type = parts[0]
                            val coordsString = parts[1]
                            val coords = coordsString.split(",").mapNotNull { it.toDoubleOrNull() }
                            if (coords.size >= 2) {
                                Geometry(type, coords)
                            } else null
                        } else null
                    }
                    // Fallback for old WKT format
                    else if (geom.startsWith("POINT", ignoreCase = true)) {
                        val coordsMatch = Regex("POINT\\s*\\(([^)]+)\\)", RegexOption.IGNORE_CASE).find(geom)
                        if (coordsMatch != null) {
                            val coords = coordsMatch.groupValues[1].trim().split("\\s+".toRegex())
                            if (coords.size >= 2) {
                                val longitude = coords[0].toDoubleOrNull()
                                val latitude = coords[1].toDoubleOrNull()
                                if (longitude != null && latitude != null) {
                                    Geometry("Point", listOf(longitude, latitude))
                                } else null
                            } else null
                        } else null
                    } else null
                }
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }
}

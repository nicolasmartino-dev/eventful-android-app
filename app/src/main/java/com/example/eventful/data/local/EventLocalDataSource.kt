package com.example.eventful.data.local

import com.example.eventful.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventLocalDataSource {
    fun getEvents(): Flow<List<Event>>
    fun getEventById(id: String): Flow<Event?>
    suspend fun insertEvents(events: List<Event>)
    suspend fun clearEvents()
}

package com.eventful.app.data.local

import com.eventful.app.data.local.EventEntity
import com.eventful.app.domain.model.Event
import com.eventful.app.data.mapper.EventMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventLocalDataSource @Inject constructor(
    private val eventDao: EventDao
) {
    fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getEvents().map { entities ->
            entities.map { EventMapper.run { it.toEvent() } }
        }
    }

    fun getEventById(id: String): Flow<Event?> {
        return eventDao.getEventByIdFlow(id).map { entity ->
            entity?.let { EventMapper.run { it.toEvent() } }
        }
    }

    suspend fun insertEvents(events: List<Event>) {
        val entities = events.map { EventMapper.run { it.toEventEntity() } }
        eventDao.insertEvents(entities)
    }

    suspend fun insertEvent(event: Event) {
        eventDao.insertEvents(listOf(EventMapper.run { event.toEventEntity() }))
    }

    suspend fun deleteAllEvents() {
        eventDao.clearEvents()
    }
}

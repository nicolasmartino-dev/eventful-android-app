package com.example.eventful.data.local

import com.example.eventful.data.mapper.EventMapper
import com.example.eventful.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventLocalDataSourceImpl @Inject constructor(
    private val eventDao: EventDao
) : EventLocalDataSource {

    override fun getEvents(): Flow<List<Event>> {
        return eventDao.getEvents().map { entities ->
            entities.map { EventMapper.run { it.toEvent() } }
        }
    }

    override fun getEventById(id: String): Flow<Event?> {
        return eventDao.getEventByIdFlow(id).map { entity ->
            entity?.let { EventMapper.run { it.toEvent() } }
        }
    }

    override suspend fun insertEvents(events: List<Event>) {
        val entities = events.map { EventMapper.run { it.toEventEntity() } }
        eventDao.insertEvents(entities)
    }

    override suspend fun clearEvents() {
        eventDao.clearEvents()
    }
}

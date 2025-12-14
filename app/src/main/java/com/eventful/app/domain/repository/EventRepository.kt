package com.eventful.app.domain.repository

import com.eventful.app.domain.model.Event
import com.eventful.app.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<Resource<List<Event>>>
    fun getEventById(id: String): Flow<Resource<Event>>
    fun loadMoreEvents(offset: Int, limit: Int = 20): Flow<Resource<List<Event>>>
}

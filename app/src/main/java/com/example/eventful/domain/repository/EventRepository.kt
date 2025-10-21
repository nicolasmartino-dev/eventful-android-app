package com.example.eventful.domain.repository

import com.example.eventful.domain.model.Event
import com.example.eventful.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<Resource<List<Event>>>
    fun getEventById(id: String): Flow<Resource<Event>>
    fun loadMoreEvents(offset: Int, limit: Int = 20): Flow<Resource<List<Event>>>
}

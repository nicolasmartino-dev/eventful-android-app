package com.example.eventful.domain.repository

import com.example.eventful.domain.model.Event
import com.example.eventful.util.Resource
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<Resource<List<Event>>>
    fun getEventById(id: String): Flow<Resource<Event>>
}

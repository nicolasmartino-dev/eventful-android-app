package com.example.eventful.data.remote

import com.example.eventful.domain.model.Event
import com.example.eventful.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface EventRemoteDataSource {
    fun getEvents(limit: Int = 20, offset: Int = 0): Flow<Resource<List<Event>>>
    fun getEventById(id: String): Flow<Resource<Event>>
}

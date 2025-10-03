package com.example.eventful.domain.usecase

import com.example.eventful.domain.model.Event
import com.example.eventful.domain.repository.EventRepository
import com.example.eventful.util.Resource
import kotlinx.coroutines.flow.Flow

class GetEventsUseCase(
    private val repository: EventRepository
) {
    operator fun invoke(): Flow<Resource<List<Event>>> {
        return repository.getEvents()
    }
}

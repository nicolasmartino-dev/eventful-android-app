package com.example.eventful.domain.usecase

import com.example.eventful.domain.model.Event
import com.example.eventful.domain.model.Resource
import com.example.eventful.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class GetEventDetailsUseCase(
    private val repository: EventRepository
) {
    operator fun invoke(id: String): Flow<Resource<Event>> {
        return repository.getEventById(id)
    }
}

package com.eventful.app.domain.usecase

import com.eventful.app.domain.model.Event
import com.eventful.app.domain.model.Resource
import com.eventful.app.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class GetEventDetailsUseCase(
    private val repository: EventRepository
) {
    operator fun invoke(id: String): Flow<Resource<Event>> {
        return repository.getEventById(id)
    }
}

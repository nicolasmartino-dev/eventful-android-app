package com.example.eventful.domain.usecase

import com.example.eventful.domain.model.Event
import com.example.eventful.domain.model.Resource
import com.example.eventful.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class LoadMoreEventsUseCase(
    private val repository: EventRepository
) {
    operator fun invoke(offset: Int, limit: Int = 20): Flow<Resource<List<Event>>> {
        return repository.loadMoreEvents(offset, limit)
    }
}

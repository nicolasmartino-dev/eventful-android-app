package com.eventful.app.domain.usecase

import com.eventful.app.domain.model.Event
import com.eventful.app.domain.model.Resource
import com.eventful.app.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class LoadMoreEventsUseCase(
    private val repository: EventRepository
) {
    operator fun invoke(offset: Int, limit: Int = 20): Flow<Resource<List<Event>>> {
        return repository.loadMoreEvents(offset, limit)
    }
}


package com.example.eventful.data.repository

import com.example.eventful.data.local.EventLocalDataSource
import com.example.eventful.data.remote.EventRemoteDataSource
import com.example.eventful.domain.model.Event
import com.example.eventful.domain.model.Resource
import com.example.eventful.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val remoteDataSource: EventRemoteDataSource,
    private val localDataSource: EventLocalDataSource
) : EventRepository {

    override fun getEvents(): Flow<Resource<List<Event>>> = flow {
        emit(Resource.Loading())

        val localEvents = localDataSource.getEvents().firstOrNull()
        if (localEvents != null && localEvents.isNotEmpty()) {
            emit(Resource.Loading(data = localEvents)) // Emit cached data while loading new
        }

        remoteDataSource.getEvents(limit = 10, offset = 0).collect { result ->
            when (result) {
                is Resource.Success -> {
                    localDataSource.clearEvents() // Clear old cache
                    localDataSource.insertEvents(result.data ?: emptyList())
                    emit(Resource.Success(result.data ?: emptyList()))
                }
                is Resource.Error -> {
                    emit(Resource.Error(result.message ?: "An unexpected error occurred", data = localEvents))
                }
                is Resource.Loading -> {
                    // Keep loading state
                }
            }
        }
    }

    override fun loadMoreEvents(offset: Int, limit: Int): Flow<Resource<List<Event>>> = flow {
        emit(Resource.Loading())

        remoteDataSource.getEvents(limit = limit, offset = offset).collect { result ->
            when (result) {
                is Resource.Success -> {
                    // Append to existing cache instead of clearing
                    localDataSource.insertEvents(result.data ?: emptyList())
                    emit(Resource.Success(result.data ?: emptyList()))
                }
                is Resource.Error -> {
                    emit(Resource.Error(result.message ?: "An unexpected error occurred"))
                }
                is Resource.Loading -> {
                    // Keep loading state
                }
            }
        }
    }

    override fun getEventById(id: String): Flow<Resource<Event>> = flow {
        emit(Resource.Loading())

        val localEvent = localDataSource.getEventById(id).firstOrNull()
        if (localEvent != null) {
            emit(Resource.Loading(data = localEvent))
        }

        remoteDataSource.getEventById(id).collect { result ->
            when (result) {
                is Resource.Success -> {
                    // Update or insert into cache
                    result.data?.let { event ->
                        localDataSource.insertEvents(listOf(event))
                        emit(Resource.Success(event))
                    } ?: emit(Resource.Error("Event data is null"))
                }
                is Resource.Error -> {
                    emit(Resource.Error(result.message ?: "An unexpected error occurred", data = localEvent))
                }
                is Resource.Loading -> {
                    // Keep loading state
                }
            }
        }
    }
}

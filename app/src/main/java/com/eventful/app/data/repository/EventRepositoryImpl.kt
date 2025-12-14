package com.eventful.app.data.repository

import com.eventful.app.data.local.EventLocalDataSource
import com.eventful.app.data.remote.EventRemoteDataSource
import com.eventful.app.domain.model.Event
import com.eventful.app.domain.model.Resource
import com.eventful.app.domain.repository.EventRepository
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
        emit(Resource.Loading)

        val localEvents = localDataSource.getAllEvents().firstOrNull()
        if (localEvents != null && localEvents.isNotEmpty()) {
            emit(Resource.Success(localEvents)) // Emit cached data while loading new
        }

        try {
            val remoteEvents = remoteDataSource.getAllEvents(limit = 10, offset = 0)
            localDataSource.deleteAllEvents() // Clear old cache
            localDataSource.insertEvents(remoteEvents)
            emit(Resource.Success(remoteEvents))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    override fun loadMoreEvents(offset: Int, limit: Int): Flow<Resource<List<Event>>> = flow {
        emit(Resource.Loading)

        try {
            val remoteEvents = remoteDataSource.getAllEvents(limit = limit, offset = offset)
            // Append to existing cache instead of clearing
            localDataSource.insertEvents(remoteEvents)
            emit(Resource.Success(remoteEvents))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    override fun getEventById(id: String): Flow<Resource<Event>> = flow {
        emit(Resource.Loading)

        val localEvent = localDataSource.getEventById(id).firstOrNull()
        if (localEvent != null) {
            emit(Resource.Success(localEvent))
        }

        try {
            val remoteEvent = remoteDataSource.getEventById(id)
            if (remoteEvent != null) {
                // Update or insert into cache
                localDataSource.insertEvent(remoteEvent)
                emit(Resource.Success(remoteEvent))
            } else {
                emit(Resource.Error(Exception("Event not found")))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}

package com.example.eventful.data.repository

import com.apollographql.apollo.ApolloClient
import com.example.eventful.apollo.AllEventsQuery
import com.example.eventful.apollo.EventDetailsQuery
import com.example.eventful.data.local.EventDao
import com.example.eventful.data.local.toEventEntity
import com.example.eventful.data.remote.dto.toEvent
import com.example.eventful.domain.model.Event
import com.example.eventful.domain.repository.EventRepository
import com.example.eventful.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val api: ApolloClient,
    private val dao: EventDao
) : EventRepository {

    override fun getEvents(): Flow<Resource<List<Event>>> = flow {
        emit(Resource.Loading())

        val localEvents = dao.getEvents().firstOrNull()?.map { it.toEvent() }
        if (localEvents != null && localEvents.isNotEmpty()) {
            emit(Resource.Loading(data = localEvents)) // Emit cached data while loading new
        }

        try {
            // Fetch first page with 10 events for faster initial load
            val response = api.query(AllEventsQuery(limit = com.apollographql.apollo.api.Optional.present(10), offset = com.apollographql.apollo.api.Optional.present(0))).execute()
            if (response.hasErrors()) {
                emit(Resource.Error(message = response.errors?.firstOrNull()?.message ?: "An unknown GraphQL error occurred"))
                return@flow
            }
            val remoteEvents = response.data?.allEvents?.mapNotNull { it?.toEvent() }
            if (remoteEvents != null) {
                dao.clearEvents() // Clear old cache
                dao.insertEvents(remoteEvents.map { it.toEventEntity() }) // Used toEventEntity()
                emit(Resource.Success(remoteEvents))
            } else {
                emit(Resource.Error(message = "No events found from remote.", data = localEvents))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection.", data = localEvents))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}", data = localEvents))
        }
    }

    override fun loadMoreEvents(offset: Int, limit: Int): Flow<Resource<List<Event>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.query(AllEventsQuery(
                limit = com.apollographql.apollo.api.Optional.present(limit), 
                offset = com.apollographql.apollo.api.Optional.present(offset)
            )).execute()
            
            if (response.hasErrors()) {
                emit(Resource.Error(message = response.errors?.firstOrNull()?.message ?: "An unknown GraphQL error occurred"))
                return@flow
            }
            
            val remoteEvents = response.data?.allEvents?.mapNotNull { it?.toEvent() }
            if (remoteEvents != null) {
                // Append to existing cache instead of clearing
                dao.insertEvents(remoteEvents.map { it.toEventEntity() })
                emit(Resource.Success(remoteEvents))
            } else {
                emit(Resource.Error(message = "No more events found."))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}"))
        }
    }

    override fun getEventById(id: String): Flow<Resource<Event>> = flow {
        emit(Resource.Loading())

        val localEvent = dao.getEventById(id)?.toEvent()
        if (localEvent != null) {
            emit(Resource.Loading(data = localEvent))
        }

        try {
            val response = api.query(EventDetailsQuery(id = id)).execute()
            if (response.hasErrors()) {
                emit(Resource.Error(message = response.errors?.firstOrNull()?.message ?: "An unknown GraphQL error occurred"))
                return@flow
            }
            val remoteEvent = response.data?.eventById?.toEvent()
            if (remoteEvent != null) {
                // Update or insert into cache (Room handles REPLACE strategy)
                dao.insertEvents(listOf(remoteEvent.toEventEntity())) // Used toEventEntity()
                emit(Resource.Success(remoteEvent))
            } else {
                emit(Resource.Error(message = "Event not found from remote.", data = localEvent))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection.", data = localEvent))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}", data = localEvent))
        }
    }
}

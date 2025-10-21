package com.example.eventful.data.remote

import com.apollographql.apollo.ApolloClient
import com.example.eventful.apollo.AllEventsQuery
import com.example.eventful.apollo.EventDetailsQuery
import com.example.eventful.data.mapper.EventMapper
import com.example.eventful.domain.model.Event
import com.example.eventful.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRemoteDataSourceImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : EventRemoteDataSource {

    override fun getEvents(limit: Int, offset: Int): Flow<Resource<List<Event>>> = flow {
        emit(Resource.Loading())

        try {
            val response = apolloClient.query(
                AllEventsQuery(
                    limit = com.apollographql.apollo.api.Optional.present(limit),
                    offset = com.apollographql.apollo.api.Optional.present(offset)
                )
            ).execute()

            if (response.hasErrors()) {
                emit(Resource.Error(message = response.errors?.firstOrNull()?.message ?: "An unknown GraphQL error occurred"))
                return@flow
            }

            val events = response.data?.allEvents?.mapNotNull { it?.let { event -> EventMapper.run { event.toEvent() } } }
            if (events != null) {
                emit(Resource.Success(events))
            } else {
                emit(Resource.Error(message = "No events found from remote."))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}"))
        }
    }

    override fun getEventById(id: String): Flow<Resource<Event>> = flow {
        emit(Resource.Loading())

        try {
            val response = apolloClient.query(EventDetailsQuery(id = id)).execute()
            if (response.hasErrors()) {
                emit(Resource.Error(message = response.errors?.firstOrNull()?.message ?: "An unknown GraphQL error occurred"))
                return@flow
            }

            val event = response.data?.eventById?.let { EventMapper.run { it.toEvent() } }
            if (event != null) {
                emit(Resource.Success(event))
            } else {
                emit(Resource.Error(message = "Event not found from remote."))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}"))
        }
    }
}

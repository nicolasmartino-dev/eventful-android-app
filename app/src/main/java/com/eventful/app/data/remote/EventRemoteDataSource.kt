package com.eventful.app.data.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.eventful.app.apollo.AllEventsQuery
import com.eventful.app.apollo.EventDetailsQuery
import com.eventful.app.domain.model.Event
import com.eventful.app.data.mapper.EventMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRemoteDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend fun getAllEvents(limit: Int? = null, offset: Int? = null): List<Event> {
        val response = apolloClient.query(
            AllEventsQuery(
                limit = if (limit != null) Optional.Present(limit) else Optional.Absent,
                offset = if (offset != null) Optional.Present(offset) else Optional.Absent
            )
        ).execute()

        return response.data?.allEvents?.mapNotNull { event ->
            event?.let { EventMapper.run { it.toEvent() } }
        } ?: emptyList()
    }

    suspend fun getEventById(id: String): Event? {
        val response = apolloClient.query(
            EventDetailsQuery(id = id)
        ).execute()

        return response.data?.eventById?.let { EventMapper.run { it.toEvent() } }
    }
}

package com.example.eventful.data.repository

import app.cash.turbine.test
import com.example.eventful.data.local.EventLocalDataSource
import com.example.eventful.data.remote.EventRemoteDataSource
import com.example.eventful.domain.model.Event
import com.example.eventful.domain.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class EventRepositoryImplTest {

    @Mock
    private lateinit var remoteDataSource: EventRemoteDataSource

    @Mock
    private lateinit var localDataSource: EventLocalDataSource

    private lateinit var repository: EventRepositoryImpl
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        repository = EventRepositoryImpl(remoteDataSource, localDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getEvents should return cached data first then remote data`() = runTest {
        // Given
        val cachedEvents = listOf(
            Event(id = "1", title = "Cached Event", startTime = "2024-01-01T10:00:00Z")
        )
        val remoteEvents = listOf(
            Event(id = "1", title = "Remote Event 1", startTime = "2024-01-01T10:00:00Z"),
            Event(id = "2", title = "Remote Event 2", startTime = "2024-01-02T10:00:00Z")
        )

        whenever(localDataSource.getEvents()).thenReturn(flowOf(cachedEvents))
        whenever(remoteDataSource.getEvents(10, 0)).thenReturn(flowOf(Resource.Success(remoteEvents)))

        // When
        repository.getEvents().test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)
            assertNull(loadingState.data)

            val cachedState = awaitItem()
            assertTrue(cachedState is Resource.Loading)
            assertEquals(cachedEvents, cachedState.data)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertEquals(remoteEvents, successState.data)

            verify(localDataSource).clearEvents()
            verify(localDataSource).insertEvents(remoteEvents)
        }
    }

    @Test
    fun `getEvents should return error when remote fails but show cached data`() = runTest {
        // Given
        val cachedEvents = listOf(
            Event(id = "1", title = "Cached Event", startTime = "2024-01-01T10:00:00Z")
        )
        val errorMessage = "Network error"

        whenever(localDataSource.getEvents()).thenReturn(flowOf(cachedEvents))
        whenever(remoteDataSource.getEvents(10, 0)).thenReturn(flowOf(Resource.Error(errorMessage)))

        // When
        repository.getEvents().test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val cachedState = awaitItem()
            assertTrue(cachedState is Resource.Loading)
            assertEquals(cachedEvents, cachedState.data)

            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)
            assertEquals(errorMessage, errorState.message)
            assertEquals(cachedEvents, errorState.data)
        }
    }

    @Test
    fun `loadMoreEvents should append new events to cache`() = runTest {
        // Given
        val newEvents = listOf(
            Event(id = "3", title = "New Event 3", startTime = "2024-01-03T10:00:00Z"),
            Event(id = "4", title = "New Event 4", startTime = "2024-01-04T10:00:00Z")
        )
        val offset = 10
        val limit = 20

        whenever(remoteDataSource.getEvents(limit, offset)).thenReturn(flowOf(Resource.Success(newEvents)))

        // When
        repository.loadMoreEvents(offset, limit).test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertEquals(newEvents, successState.data)

            verify(localDataSource).insertEvents(newEvents)
        }
    }

    @Test
    fun `getEventById should return cached data first then remote data`() = runTest {
        // Given
        val eventId = "test-event-id"
        val cachedEvent = Event(id = eventId, title = "Cached Event", startTime = "2024-01-01T10:00:00Z")
        val remoteEvent = Event(id = eventId, title = "Remote Event", startTime = "2024-01-01T10:00:00Z")

        whenever(localDataSource.getEventById(eventId)).thenReturn(flowOf(cachedEvent))
        whenever(remoteDataSource.getEventById(eventId)).thenReturn(flowOf(Resource.Success(remoteEvent)))

        // When
        repository.getEventById(eventId).test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)
            assertNull(loadingState.data)

            val cachedState = awaitItem()
            assertTrue(cachedState is Resource.Loading)
            assertEquals(cachedEvent, cachedState.data)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertEquals(remoteEvent, successState.data)

            verify(localDataSource).insertEvents(listOf(remoteEvent))
        }
    }

    @Test
    fun `getEventById should return error when remote fails but show cached data`() = runTest {
        // Given
        val eventId = "test-event-id"
        val cachedEvent = Event(id = eventId, title = "Cached Event", startTime = "2024-01-01T10:00:00Z")
        val errorMessage = "Event not found"

        whenever(localDataSource.getEventById(eventId)).thenReturn(flowOf(cachedEvent))
        whenever(remoteDataSource.getEventById(eventId)).thenReturn(flowOf(Resource.Error(errorMessage)))

        // When
        repository.getEventById(eventId).test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val cachedState = awaitItem()
            assertTrue(cachedState is Resource.Loading)
            assertEquals(cachedEvent, cachedState.data)

            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)
            assertEquals(errorMessage, errorState.message)
            assertEquals(cachedEvent, errorState.data)
        }
    }
}

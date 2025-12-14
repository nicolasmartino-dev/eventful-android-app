package com.eventful.app.data.repository

import app.cash.turbine.test
import com.eventful.app.TestHelpers.createTestEvent
import com.eventful.app.data.local.EventLocalDataSource
import com.eventful.app.data.remote.EventRemoteDataSource
import com.eventful.app.domain.model.Event
import com.eventful.app.domain.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

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
            createTestEvent(id = "1", title = "Cached Event", startTime = "2024-01-01T10:00:00Z")
        )
        val remoteEvents = listOf(
            createTestEvent(id = "1", title = "Remote Event 1", startTime = "2024-01-01T10:00:00Z"),
            createTestEvent(id = "2", title = "Remote Event 2", startTime = "2024-01-02T10:00:00Z")
        )

        whenever(localDataSource.getAllEvents()).thenReturn(flowOf(cachedEvents))
        whenever(remoteDataSource.getAllEvents(10, 0)).thenReturn(remoteEvents)

        // When
        repository.getEvents().test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val cachedState = awaitItem()
            assertTrue(cachedState is Resource.Success)
            assertEquals(cachedEvents, (cachedState as Resource.Success).data)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertEquals(remoteEvents, (successState as Resource.Success).data)

            verify(localDataSource).deleteAllEvents()
            verify(localDataSource).insertEvents(remoteEvents)
            
            awaitComplete()
        }
    }

    @Test
    fun `getEvents should return error when remote fails but show cached data`() = runTest {
        // Given
        val cachedEvents = listOf(
            createTestEvent(id = "1", title = "Cached Event", startTime = "2024-01-01T10:00:00Z")
        )
        val errorMessage = "Network error"

        whenever(localDataSource.getAllEvents()).thenReturn(flowOf(cachedEvents))
        whenever(remoteDataSource.getAllEvents(10, 0)).thenThrow(RuntimeException(errorMessage))

        // When
        repository.getEvents().test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val cachedState = awaitItem()
            assertTrue(cachedState is Resource.Success)
            assertEquals(cachedEvents, (cachedState as Resource.Success).data)

            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)
            assertEquals(errorMessage, (errorState as Resource.Error).exception.message)
            
            awaitComplete()
        }
    }

    @Test
    fun `loadMoreEvents should append new events to cache`() = runTest {
        // Given
        val newEvents = listOf(
            createTestEvent(id = "3", title = "New Event 3", startTime = "2024-01-03T10:00:00Z"),
            createTestEvent(id = "4", title = "New Event 4", startTime = "2024-01-04T10:00:00Z")
        )
        val offset = 10
        val limit = 20

        whenever(remoteDataSource.getAllEvents(limit, offset)).thenReturn(newEvents)

        // When
        repository.loadMoreEvents(offset, limit).test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertEquals(newEvents, (successState as Resource.Success).data)

            verify(localDataSource).insertEvents(newEvents)
            
            awaitComplete()
        }
    }

    @Test
    fun `getEventById should return cached data first then remote data`() = runTest {
        // Given
        val eventId = "test-event-id"
        val cachedEvent = createTestEvent(id = eventId, title = "Cached Event", startTime = "2024-01-01T10:00:00Z")
        val remoteEvent = createTestEvent(id = eventId, title = "Remote Event", startTime = "2024-01-01T10:00:00Z")

        whenever(localDataSource.getEventById(eventId)).thenReturn(flowOf(cachedEvent))
        whenever(remoteDataSource.getEventById(eventId)).thenReturn(remoteEvent)

        // When
        repository.getEventById(eventId).test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val cachedState = awaitItem()
            assertTrue(cachedState is Resource.Success)
            assertEquals(cachedEvent, (cachedState as Resource.Success).data)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertEquals(remoteEvent, (successState as Resource.Success).data)

            verify(localDataSource).insertEvent(remoteEvent)
            
            awaitComplete()
        }
    }

    @Test
    fun `getEventById should return error when remote fails but show cached data`() = runTest {
        // Given
        val eventId = "test-event-id"
        val cachedEvent = createTestEvent(id = eventId, title = "Cached Event", startTime = "2024-01-01T10:00:00Z")
        val errorMessage = "Event not found"

        whenever(localDataSource.getEventById(eventId)).thenReturn(flowOf(cachedEvent))
        whenever(remoteDataSource.getEventById(eventId)).thenThrow(RuntimeException(errorMessage))

        // When
        repository.getEventById(eventId).test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val cachedState = awaitItem()
            assertTrue(cachedState is Resource.Success)
            assertEquals(cachedEvent, (cachedState as Resource.Success).data)

            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)
            assertEquals(errorMessage, (errorState as Resource.Error).exception.message)
            
            awaitComplete()
        }
    }
}

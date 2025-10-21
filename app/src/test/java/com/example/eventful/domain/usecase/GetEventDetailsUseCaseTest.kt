package com.example.eventful.domain.usecase

import com.example.eventful.domain.model.Event
import com.example.eventful.domain.model.Resource
import com.example.eventful.domain.repository.EventRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetEventDetailsUseCaseTest {

    @Mock
    private lateinit var repository: EventRepository

    private lateinit var getEventDetailsUseCase: GetEventDetailsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getEventDetailsUseCase = GetEventDetailsUseCase(repository)
    }

    @Test
    fun `invoke should return success when repository returns success`() = runTest {
        // Given
        val eventId = "test-event-id"
        val mockEvent = Event(
            id = eventId,
            title = "Test Event",
            startTime = "2024-01-01T10:00:00Z",
            description = "Test Description"
        )
        val expectedResult = Resource.Success(mockEvent)
        whenever(repository.getEventById(eventId)).thenReturn(flowOf(expectedResult))

        // When
        val result = getEventDetailsUseCase(eventId).collect { }

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(mockEvent, result.data)
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Given
        val eventId = "test-event-id"
        val errorMessage = "Event not found"
        val expectedResult = Resource.Error<Event>(errorMessage)
        whenever(repository.getEventById(eventId)).thenReturn(flowOf(expectedResult))

        // When
        val result = getEventDetailsUseCase(eventId).collect { }

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, result.message)
    }

    @Test
    fun `invoke should return loading when repository returns loading`() = runTest {
        // Given
        val eventId = "test-event-id"
        val expectedResult = Resource.Loading<Event>()
        whenever(repository.getEventById(eventId)).thenReturn(flowOf(expectedResult))

        // When
        val result = getEventDetailsUseCase(eventId).collect { }

        // Then
        assertTrue(result is Resource.Loading)
    }
}

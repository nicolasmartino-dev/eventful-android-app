package com.eventful.app.domain.usecase

import com.eventful.app.TestHelpers.createTestEvent
import com.eventful.app.domain.model.Event
import com.eventful.app.domain.model.Resource
import com.eventful.app.domain.repository.EventRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

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
        val mockEvent = createTestEvent(
            id = eventId,
            title = "Test Event",
            startTime = "2024-01-01T10:00:00Z",
            description = "Test Description"
        )
        val expectedResult = Resource.Success(mockEvent)
        whenever(repository.getEventById(eventId)).thenReturn(flowOf(expectedResult))

        // When
        val result = getEventDetailsUseCase(eventId).first()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(mockEvent, (result as Resource.Success).data)
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Given
        val eventId = "test-event-id"
        val errorMessage = "Event not found"
        val expectedResult = Resource.Error(Throwable(errorMessage))
        whenever(repository.getEventById(eventId)).thenReturn(flowOf(expectedResult))

        // When
        val result = getEventDetailsUseCase(eventId).first()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).exception.message)
    }

    @Test
    fun `invoke should return loading when repository returns loading`() = runTest {
        // Given
        val eventId = "test-event-id"
        val expectedResult = Resource.Loading
        whenever(repository.getEventById(eventId)).thenReturn(flowOf(expectedResult))

        // When
        val result = getEventDetailsUseCase(eventId).first()

        // Then
        assertTrue(result is Resource.Loading)
    }
}

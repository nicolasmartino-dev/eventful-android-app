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

class GetEventsUseCaseTest {

    @Mock
    private lateinit var repository: EventRepository

    private lateinit var getEventsUseCase: GetEventsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getEventsUseCase = GetEventsUseCase(repository)
    }

    @Test
    fun `invoke should return success when repository returns success`() = runTest {
        // Given
        val mockEvents = listOf(
            Event(
                id = "1",
                title = "Test Event 1",
                startTime = "2024-01-01T10:00:00Z"
            ),
            Event(
                id = "2",
                title = "Test Event 2",
                startTime = "2024-01-02T10:00:00Z"
            )
        )
        val expectedResult = Resource.Success(mockEvents)
        whenever(repository.getEvents()).thenReturn(flowOf(expectedResult))

        // When
        val result = getEventsUseCase().collect { }

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(mockEvents, result.data)
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Given
        val errorMessage = "Network error"
        val expectedResult = Resource.Error<List<Event>>(errorMessage)
        whenever(repository.getEvents()).thenReturn(flowOf(expectedResult))

        // When
        val result = getEventsUseCase().collect { }

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, result.message)
    }

    @Test
    fun `invoke should return loading when repository returns loading`() = runTest {
        // Given
        val expectedResult = Resource.Loading<List<Event>>()
        whenever(repository.getEvents()).thenReturn(flowOf(expectedResult))

        // When
        val result = getEventsUseCase().collect { }

        // Then
        assertTrue(result is Resource.Loading)
    }
}

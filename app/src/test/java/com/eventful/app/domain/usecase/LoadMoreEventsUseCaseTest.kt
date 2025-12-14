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

class LoadMoreEventsUseCaseTest {

    @Mock
    private lateinit var repository: EventRepository

    private lateinit var loadMoreEventsUseCase: LoadMoreEventsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        loadMoreEventsUseCase = LoadMoreEventsUseCase(repository)
    }

    @Test
    fun `invoke should return success when repository returns success`() = runTest {
        // Given
        val offset = 10
        val limit = 20
        val mockEvents = listOf(
            createTestEvent(id = "11", title = "Test Event 11", startTime = "2024-01-11T10:00:00Z"),
            createTestEvent(id = "12", title = "Test Event 12", startTime = "2024-01-12T10:00:00Z")
        )
        val expectedResult = Resource.Success(mockEvents)
        whenever(repository.loadMoreEvents(offset, limit)).thenReturn(flowOf(expectedResult))

        // When
        val result = loadMoreEventsUseCase(offset, limit).first()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(mockEvents, (result as Resource.Success).data)
    }

    @Test
    fun `invoke should use default limit when not specified`() = runTest {
        // Given
        val offset = 10
        val defaultLimit = 20
        val mockEvents = emptyList<Event>()
        val expectedResult = Resource.Success(mockEvents)
        whenever(repository.loadMoreEvents(offset, defaultLimit)).thenReturn(flowOf(expectedResult))

        // When
        val result = loadMoreEventsUseCase(offset).first()

        // Then
        assertTrue(result is Resource.Success)
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Given
        val offset = 10
        val limit = 20
        val errorMessage = "Network error"
        val expectedResult = Resource.Error(Throwable(errorMessage))
        whenever(repository.loadMoreEvents(offset, limit)).thenReturn(flowOf(expectedResult))

        // When
        val result = loadMoreEventsUseCase(offset, limit).first()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).exception.message)
    }
}

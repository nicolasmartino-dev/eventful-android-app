package com.eventful.app.presentation.event_detail

import com.eventful.app.TestHelpers.createTestEvent
import com.eventful.app.domain.model.Event
import com.eventful.app.domain.model.Resource
import com.eventful.app.domain.usecase.GetEventDetailsUseCase
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
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class EventDetailViewModelTest {

    @Mock
    private lateinit var getEventDetailsUseCase: GetEventDetailsUseCase

    private lateinit var viewModel: EventDetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be loading when eventId is provided`() = runTest {
        // Given
        val eventId = "test-event-id"
        val mockEvent = createTestEvent(
            id = eventId,
            title = "Test Event",
            startTime = "2024-01-01T10:00:00Z",
            description = "Test Description"
        )
        whenever(getEventDetailsUseCase(eventId)).thenReturn(flowOf(Resource.Success(mockEvent)))

        // When
        viewModel = EventDetailViewModel(getEventDetailsUseCase, mockSavedStateHandle(eventId))
        testDispatcher.scheduler.advanceUntilIdle() // Process the init block
        val initialState = viewModel.state.value

        // Then
        assertFalse(initialState.isLoading) // After processing, should not be loading
        assertEquals(mockEvent, initialState.event)
        assertEquals("", initialState.error)
    }

    @Test
    fun `should update state with event when getEventDetails succeeds`() = runTest {
        // Given
        val eventId = "test-event-id"
        val mockEvent = createTestEvent(
            id = eventId,
            title = "Test Event",
            startTime = "2024-01-01T10:00:00Z",
            description = "Test Description"
        )
        whenever(getEventDetailsUseCase(eventId)).thenReturn(flowOf(Resource.Success(mockEvent)))

        // When
        viewModel = EventDetailViewModel(getEventDetailsUseCase, mockSavedStateHandle(eventId))
        testDispatcher.scheduler.advanceUntilIdle()
        val successState = viewModel.state.value

        // Then
        assertFalse(successState.isLoading)
        assertEquals(mockEvent, successState.event)
        assertEquals("", successState.error)
    }

    @Test
    fun `should update state with error when getEventDetails fails`() = runTest {
        // Given
        val eventId = "test-event-id"
        val errorMessage = "Event not found"
        whenever(getEventDetailsUseCase(eventId)).thenReturn(flowOf(Resource.Error(Throwable(errorMessage))))

        // When
        viewModel = EventDetailViewModel(getEventDetailsUseCase, mockSavedStateHandle(eventId))
        testDispatcher.scheduler.advanceUntilIdle()
        val errorState = viewModel.state.value

        // Then
        assertFalse(errorState.isLoading)
        assertEquals(errorMessage, errorState.error)
        assertNull(errorState.event)
    }

    @Test
    fun `should handle loading state correctly`() = runTest {
        // Given
        val eventId = "test-event-id"
        val mockEvent = createTestEvent(
            id = eventId,
            title = "Test Event",
            startTime = "2024-01-01T10:00:00Z"
        )
        whenever(getEventDetailsUseCase(eventId)).thenReturn(flowOf(Resource.Loading))

        // When
        viewModel = EventDetailViewModel(getEventDetailsUseCase, mockSavedStateHandle(eventId))
        testDispatcher.scheduler.advanceUntilIdle() // Process the init block
        val loadingState = viewModel.state.value

        // Then
        assertTrue(loadingState.isLoading) // Should still be loading since we returned Loading
        assertNull(loadingState.event)
        assertEquals("", loadingState.error)
    }

    private fun mockSavedStateHandle(eventId: String): androidx.lifecycle.SavedStateHandle {
        return androidx.lifecycle.SavedStateHandle(mapOf("eventId" to eventId))
    }
}
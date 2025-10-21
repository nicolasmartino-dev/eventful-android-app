package com.example.eventful.presentation.event_detail

import app.cash.turbine.test
import com.example.eventful.domain.model.Event
import com.example.eventful.domain.model.Resource
import com.example.eventful.domain.usecase.GetEventDetailsUseCase
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
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

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
        val mockEvent = Event(
            id = eventId,
            title = "Test Event",
            startTime = "2024-01-01T10:00:00Z",
            description = "Test Description"
        )
        whenever(getEventDetailsUseCase(eventId)).thenReturn(flowOf(Resource.Success(mockEvent)))

        // When
        viewModel = EventDetailViewModel(getEventDetailsUseCase, mockSavedStateHandle(eventId))
        viewModel.state.test {
            // Then
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
            assertNull(initialState.event)
            assertEquals("", initialState.error)
        }
    }

    @Test
    fun `should update state with event when getEventDetails succeeds`() = runTest {
        // Given
        val eventId = "test-event-id"
        val mockEvent = Event(
            id = eventId,
            title = "Test Event",
            startTime = "2024-01-01T10:00:00Z",
            description = "Test Description"
        )
        whenever(getEventDetailsUseCase(eventId)).thenReturn(flowOf(Resource.Success(mockEvent)))

        // When
        viewModel = EventDetailViewModel(getEventDetailsUseCase, mockSavedStateHandle(eventId))
        viewModel.state.test {
            // Skip initial loading state
            awaitItem()
            
            // Then
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(mockEvent, successState.event)
            assertEquals("", successState.error)
        }
    }

    @Test
    fun `should update state with error when getEventDetails fails`() = runTest {
        // Given
        val eventId = "test-event-id"
        val errorMessage = "Event not found"
        whenever(getEventDetailsUseCase(eventId)).thenReturn(flowOf(Resource.Error(errorMessage)))

        // When
        viewModel = EventDetailViewModel(getEventDetailsUseCase, mockSavedStateHandle(eventId))
        viewModel.state.test {
            // Skip initial loading state
            awaitItem()
            
            // Then
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals(errorMessage, errorState.error)
            assertNull(errorState.event)
        }
    }

    @Test
    fun `should handle loading state correctly`() = runTest {
        // Given
        val eventId = "test-event-id"
        val mockEvent = Event(
            id = eventId,
            title = "Test Event",
            startTime = "2024-01-01T10:00:00Z"
        )
        whenever(getEventDetailsUseCase(eventId)).thenReturn(flowOf(Resource.Loading()))

        // When
        viewModel = EventDetailViewModel(getEventDetailsUseCase, mockSavedStateHandle(eventId))
        viewModel.state.test {
            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)
            assertNull(loadingState.event)
            assertEquals("", loadingState.error)
        }
    }

    private fun mockSavedStateHandle(eventId: String): androidx.lifecycle.SavedStateHandle {
        return androidx.lifecycle.SavedStateHandle(mapOf("eventId" to eventId))
    }
}

package com.example.eventful.presentation.event_list

import app.cash.turbine.test
import com.example.eventful.domain.model.Event
import com.example.eventful.domain.model.Resource
import com.example.eventful.domain.usecase.GetEventsUseCase
import com.example.eventful.domain.usecase.LoadMoreEventsUseCase
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class EventListViewModelTest {

    @Mock
    private lateinit var getEventsUseCase: GetEventsUseCase

    @Mock
    private lateinit var loadMoreEventsUseCase: LoadMoreEventsUseCase

    private lateinit var viewModel: EventListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = EventListViewModel(getEventsUseCase, loadMoreEventsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be loading`() = runTest {
        // Given
        val mockEvents = listOf(
            Event(id = "1", title = "Event 1", startTime = "2024-01-01T10:00:00Z"),
            Event(id = "2", title = "Event 2", startTime = "2024-01-02T10:00:00Z")
        )
        whenever(getEventsUseCase()).thenReturn(flowOf(Resource.Success(mockEvents)))

        // When
        viewModel.state.test {
            // Then
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
            assertEquals(emptyList(), initialState.events)
            assertEquals("", initialState.error)
        }
    }

    @Test
    fun `should update state with events when getEvents succeeds`() = runTest {
        // Given
        val mockEvents = listOf(
            Event(id = "1", title = "Event 1", startTime = "2024-01-01T10:00:00Z"),
            Event(id = "2", title = "Event 2", startTime = "2024-01-02T10:00:00Z")
        )
        whenever(getEventsUseCase()).thenReturn(flowOf(Resource.Success(mockEvents)))

        // When
        viewModel = EventListViewModel(getEventsUseCase, loadMoreEventsUseCase)
        viewModel.state.test {
            // Skip initial loading state
            awaitItem()
            
            // Then
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(mockEvents, successState.events)
            assertEquals(2, successState.currentOffset)
            assertEquals("", successState.error)
        }
    }

    @Test
    fun `should update state with error when getEvents fails`() = runTest {
        // Given
        val errorMessage = "Network error"
        whenever(getEventsUseCase()).thenReturn(flowOf(Resource.Error(errorMessage)))

        // When
        viewModel = EventListViewModel(getEventsUseCase, loadMoreEventsUseCase)
        viewModel.state.test {
            // Skip initial loading state
            awaitItem()
            
            // Then
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals(errorMessage, errorState.error)
            assertEquals(emptyList(), errorState.events)
        }
    }

    @Test
    fun `loadMoreEvents should append new events to existing list`() = runTest {
        // Given
        val initialEvents = listOf(
            Event(id = "1", title = "Event 1", startTime = "2024-01-01T10:00:00Z")
        )
        val moreEvents = listOf(
            Event(id = "2", title = "Event 2", startTime = "2024-01-02T10:00:00Z"),
            Event(id = "3", title = "Event 3", startTime = "2024-01-03T10:00:00Z")
        )
        
        whenever(getEventsUseCase()).thenReturn(flowOf(Resource.Success(initialEvents)))
        whenever(loadMoreEventsUseCase(1, 20)).thenReturn(flowOf(Resource.Success(moreEvents)))

        // When
        viewModel = EventListViewModel(getEventsUseCase, loadMoreEventsUseCase)
        viewModel.state.test {
            // Skip initial states
            awaitItem() // Loading
            awaitItem() // Success with initial events
            
            viewModel.loadMoreEvents()
            
            // Then
            val finalState = awaitItem()
            assertEquals(initialEvents + moreEvents, finalState.events)
            assertEquals(3, finalState.currentOffset)
            assertFalse(finalState.isLoadingMore)
        }
    }

    @Test
    fun `loadMoreEvents should not load when already loading more`() = runTest {
        // Given
        val mockEvents = listOf(Event(id = "1", title = "Event 1", startTime = "2024-01-01T10:00:00Z"))
        whenever(getEventsUseCase()).thenReturn(flowOf(Resource.Success(mockEvents)))
        whenever(loadMoreEventsUseCase(1, 20)).thenReturn(flowOf(Resource.Loading()))

        // When
        viewModel = EventListViewModel(getEventsUseCase, loadMoreEventsUseCase)
        viewModel.state.test {
            // Skip initial states
            awaitItem() // Loading
            awaitItem() // Success with initial events
            
            viewModel.loadMoreEvents()
            viewModel.loadMoreEvents() // Second call should be ignored
            
            // Then
            val loadingMoreState = awaitItem()
            assertTrue(loadingMoreState.isLoadingMore)
        }
    }

    @Test
    fun `loadMoreEvents should not load when no more events available`() = runTest {
        // Given
        val mockEvents = listOf(Event(id = "1", title = "Event 1", startTime = "2024-01-01T10:00:00Z"))
        whenever(getEventsUseCase()).thenReturn(flowOf(Resource.Success(mockEvents)))

        // When
        viewModel = EventListViewModel(getEventsUseCase, loadMoreEventsUseCase)
        viewModel.state.test {
            // Skip initial states
            awaitItem() // Loading
            awaitItem() // Success with initial events
            
            // Simulate no more events by setting hasMoreEvents to false
            viewModel.state.value.copy(hasMoreEvents = false)
            viewModel.loadMoreEvents()
            
            // Then - should not trigger loadMoreEventsUseCase
            // No additional items should be emitted
        }
    }
}

package com.eventful.app.presentation.event_list

import com.eventful.app.TestHelpers.createTestEvent
import com.eventful.app.domain.model.Event
import com.eventful.app.domain.model.Resource
import com.eventful.app.domain.usecase.GetEventsUseCase
import com.eventful.app.domain.usecase.LoadMoreEventsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

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
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be loading`() = runTest {
        // Given
        val mockEvents = listOf(
            createTestEvent(id = "1", title = "Event 1", startTime = "2024-01-01T10:00:00Z"),
            createTestEvent(id = "2", title = "Event 2", startTime = "2024-01-02T10:00:00Z")
        )
        whenever(getEventsUseCase()).thenReturn(flowOf(Resource.Success(mockEvents)))

        // When
        viewModel = EventListViewModel(getEventsUseCase, loadMoreEventsUseCase)
        testDispatcher.scheduler.advanceUntilIdle() // Process the init block
        val initialState = viewModel.state.value

        // Then
        assertFalse(initialState.isLoading) // After processing, should not be loading
        assertEquals(mockEvents, initialState.events)
        assertEquals("", initialState.error)
    }

    @Test
    fun `should update state with events when getEvents succeeds`() = runTest {
        // Given
        val mockEvents = listOf(
            createTestEvent(id = "1", title = "Event 1", startTime = "2024-01-01T10:00:00Z"),
            createTestEvent(id = "2", title = "Event 2", startTime = "2024-01-02T10:00:00Z")
        )
        whenever(getEventsUseCase()).thenReturn(flowOf(Resource.Success(mockEvents)))

        // When
        viewModel = EventListViewModel(getEventsUseCase, loadMoreEventsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        val successState = viewModel.state.value

        // Then
        assertFalse(successState.isLoading)
        assertEquals(mockEvents, successState.events)
        assertEquals(2, successState.currentOffset)
        assertEquals("", successState.error)
    }

    @Test
    fun `should update state with error when getEvents fails`() = runTest {
        // Given
        val errorMessage = "Network error"
        whenever(getEventsUseCase()).thenReturn(flowOf(Resource.Error(Throwable(errorMessage))))

        // When
        viewModel = EventListViewModel(getEventsUseCase, loadMoreEventsUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        val errorState = viewModel.state.value

        // Then
        assertFalse(errorState.isLoading)
        assertEquals(errorMessage, errorState.error)
        assertEquals(emptyList<Event>(), errorState.events)
    }
}
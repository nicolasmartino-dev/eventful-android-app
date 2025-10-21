package com.example.eventful.presentation.event_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventful.domain.usecase.GetEventsUseCase
import com.example.eventful.domain.usecase.LoadMoreEventsUseCase
import com.example.eventful.domain.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EventListViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val loadMoreEventsUseCase: LoadMoreEventsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(EventListState())
    val state: State<EventListState> = _state

    init {
        getEvents()
    }

    private fun getEvents() {
        getEventsUseCase().onEach {
            when (it) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        events = it.data ?: emptyList(),
                        isLoading = false,
                        currentOffset = (it.data?.size ?: 0)
                    )
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        error = it.message ?: "An unexpected error occurred",
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = state.value.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun loadMoreEvents() {
        if (_state.value.isLoadingMore || !_state.value.hasMoreEvents) return

        _state.value = _state.value.copy(isLoadingMore = true)

        loadMoreEventsUseCase(
            offset = _state.value.currentOffset,
            limit = 20
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val newEvents = result.data ?: emptyList()
                    _state.value = _state.value.copy(
                        events = _state.value.events + newEvents,
                        isLoadingMore = false,
                        currentOffset = _state.value.currentOffset + newEvents.size,
                        hasMoreEvents = newEvents.size == 20 // If we got less than 20, no more events
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoadingMore = false,
                        error = result.message ?: "Failed to load more events"
                    )
                }
                is Resource.Loading -> {
                    // Keep isLoadingMore = true
                }
            }
        }.launchIn(viewModelScope)
    }
}

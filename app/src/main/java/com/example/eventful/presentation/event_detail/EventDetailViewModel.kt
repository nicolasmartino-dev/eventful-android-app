package com.example.eventful.presentation.event_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventful.domain.usecase.GetEventDetailsUseCase
import com.example.eventful.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val getEventDetailsUseCase: GetEventDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(EventDetailState())
    val state: State<EventDetailState> = _state

    init {
        savedStateHandle.get<String>("eventId")?.let { eventId ->
            getEventDetails(eventId)
        }
    }

    private fun getEventDetails(eventId: String) {
        getEventDetailsUseCase(eventId).onEach {
            when (it) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        event = it.data,
                        isLoading = false
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
}

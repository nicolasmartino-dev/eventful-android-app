package com.eventful.app.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchFilterViewModel @Inject constructor(
    // Inject use cases when implemented
) : ViewModel() {
    
    private val _state = mutableStateOf(SearchFilterState())
    val state: State<SearchFilterState> = _state
    
    fun onSearchQueryChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }
    
    fun onCategoryToggle(category: String) {
        val currentCategories = _state.value.selectedCategories.toMutableSet()
        if (currentCategories.contains(category)) {
            currentCategories.remove(category)
        } else {
            currentCategories.add(category)
        }
        _state.value = _state.value.copy(selectedCategories = currentCategories)
    }
    
    fun onDateFilterChange(dateFilter: DateFilter) {
        _state.value = _state.value.copy(selectedDateFilter = dateFilter)
    }
    
    fun onPriceFilterChange(priceFilter: PriceFilter) {
        _state.value = _state.value.copy(selectedPriceFilter = priceFilter)
    }
    
    fun onRadiusChange(radius: Int) {
        _state.value = _state.value.copy(selectedRadius = radius)
    }
    
    fun clearAllFilters() {
        _state.value = SearchFilterState() // Reset to default state
    }
}







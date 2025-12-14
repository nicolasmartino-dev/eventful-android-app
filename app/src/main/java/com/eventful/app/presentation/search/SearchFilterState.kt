package com.eventful.app.presentation.search

data class SearchFilterState(
    val searchQuery: String = "",
    val selectedCategories: Set<String> = emptySet(),
    val selectedDateFilter: DateFilter = DateFilter.THIS_WEEK,
    val selectedPriceFilter: PriceFilter = PriceFilter.ANY_PRICE,
    val selectedRadius: Int = 25 // km
)







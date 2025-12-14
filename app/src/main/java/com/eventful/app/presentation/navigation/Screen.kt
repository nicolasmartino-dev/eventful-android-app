package com.eventful.app.presentation.navigation

sealed class Screen(val route: String) {
    data object EventListScreen : Screen("event_list_screen")
    data object EventDetailScreen : Screen("event_detail_screen/{eventId}") {
        fun createRoute(eventId: String) = "event_detail_screen/$eventId"
    }
    data object ProfileScreen : Screen("profile_screen")
}







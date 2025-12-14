package com.eventful.app.presentation.profile

data class ProfileState(
    val userName: String = "Guest",
    val userEmail: String = "",
    val userAvatarUrl: String? = null,
    val selectedLanguage: String = "EN", // EN or FR
    val isDarkMode: Boolean = false,
    val isLoading: Boolean = false,
    val error: String = ""
)







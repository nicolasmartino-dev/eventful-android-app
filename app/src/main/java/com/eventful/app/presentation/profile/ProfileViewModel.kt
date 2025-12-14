package com.eventful.app.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    // Inject use cases when implemented
) : ViewModel() {
    
    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state
    
    init {
        loadUserProfile()
    }
    
    private fun loadUserProfile() {
        // TODO: Load from repository/preferences
        _state.value = _state.value.copy(
            userName = "John Doe",
            userEmail = "john@email.com",
            selectedLanguage = "EN",
            isDarkMode = false
        )
    }
    
    fun onEditProfile() {
        // TODO: Navigate to edit profile screen
    }
    
    fun navigateToSavedEvents() {
        // TODO: Navigate to saved events screen
    }
    
    fun navigateToNotificationSettings() {
        // TODO: Navigate to notification settings
    }
    
    fun navigateToLocationPreferences() {
        // TODO: Navigate to location preferences
    }
    
    fun navigateToAboutHelp() {
        // TODO: Navigate to about & help screen
    }
    
    fun onLanguageChange(language: String) {
        _state.value = _state.value.copy(selectedLanguage = language)
        // TODO: Save to preferences and update app locale
    }
    
    fun onDarkModeToggle(enabled: Boolean) {
        _state.value = _state.value.copy(isDarkMode = enabled)
        // TODO: Save to preferences and update app theme
    }
    
    fun onSignOut() {
        // TODO: Implement sign out logic
        // Clear user data, navigate to login/home
    }
}







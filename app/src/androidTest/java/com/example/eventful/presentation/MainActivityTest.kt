package com.example.eventful.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.eventful.presentation.event_list.EventListScreen
import com.example.eventful.presentation.ui.theme.EventfulAppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun eventListScreen_displaysCorrectly() {
        // When
        composeTestRule.setContent {
            EventfulAppTheme {
                EventListScreen(navController = mockNavController())
            }
        }

        // Then - Check for loading indicator or empty state
        // Since we don't have real data in tests, we check for basic UI elements
        composeTestRule.onNodeWithText("Event Details").assertIsDisplayed()
    }

    private fun mockNavController(): androidx.navigation.NavController {
        return object : androidx.navigation.NavController(
            androidx.test.core.app.ApplicationProvider.getApplicationContext()
        ) {
            override fun navigate(route: String) {}
            override fun popBackStack(): Boolean = true
        }
    }
}

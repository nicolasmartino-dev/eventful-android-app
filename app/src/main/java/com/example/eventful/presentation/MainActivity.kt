package com.example.eventful.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eventful.presentation.event_detail.EventDetailScreen
import com.example.eventful.presentation.event_list.EventListScreen
import com.example.eventful.presentation.ui.theme.EventfulAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventfulAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "event_list_screen"
                    ) {
                        composable("event_list_screen") {
                            EventListScreen(navController = navController)
                        }
                        composable(
                            "event_detail_screen/{eventId}",
                            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
                        ) {
                            EventDetailScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

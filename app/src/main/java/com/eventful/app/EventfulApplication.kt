package com.eventful.app

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EventfulApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Google Maps SDK
        try {
            MapsInitializer.initialize(this)
            android.util.Log.d("EventfulApplication", "Google Maps SDK initialized successfully")
            
            // Log API key status (first few characters only for security)
            val apiKey = com.eventful.app.BuildConfig.GOOGLE_MAPS_API_KEY
            if (apiKey.isNotEmpty()) {
                android.util.Log.d("EventfulApplication", "Google Maps API key loaded: ${apiKey.take(10)}...")
                android.util.Log.d("EventfulApplication", "API key length: ${apiKey.length}")
            } else {
                android.util.Log.w("EventfulApplication", "Google Maps API key is empty or not loaded")
            }
        } catch (e: Exception) {
            android.util.Log.e("EventfulApplication", "Failed to initialize Google Maps SDK: ${e.message}", e)
        }
    }
}

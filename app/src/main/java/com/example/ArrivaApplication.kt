package com.example

import android.app.Application
import android.util.Log
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback

/**
 * ArrivaApplication - Application entry point.
 * Guarantees early initialization of Google Maps SDK with dual-renderer support:
 * - Attempts MapsInitializer.Renderer.LATEST
 * - Gracefully falls back to MapsInitializer.Renderer.LEGACY if LATEST cannot be initialized
 */
class ArrivaApplication : Application(), OnMapsSdkInitializedCallback {

    override fun onCreate() {
        super.onCreate()
        initializeGoogleMapsSdk()
    }

    private fun initializeGoogleMapsSdk() {
        try {
            MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, this)
            Log.d("ArrivaApplication", "Google Maps SDK LATEST initialization requested")
        } catch (e: Exception) {
            Log.w("ArrivaApplication", "Failed to initialize LATEST Maps renderer, trying LEGACY", e)
            try {
                MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LEGACY, this)
                Log.d("ArrivaApplication", "Google Maps SDK LEGACY initialization requested")
            } catch (ex: Exception) {
                Log.e("ArrivaApplication", "Failed to initialize Google Maps SDK with any renderer", ex)
            }
        }
    }

    override fun onMapsSdkInitialized(renderer: MapsInitializer.Renderer) {
        when (renderer) {
            MapsInitializer.Renderer.LATEST -> {
                Log.i("ArrivaApplication", "Google Maps SDK successfully initialized with LATEST renderer.")
            }
            MapsInitializer.Renderer.LEGACY -> {
                Log.i("ArrivaApplication", "Google Maps SDK initialized with LEGACY renderer.")
            }
        }
    }
}

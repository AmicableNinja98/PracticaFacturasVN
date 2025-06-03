package com.example.core.firebase

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

object RemoteConfigManager {
    private val remoteConfig by lazy {
        Firebase.remoteConfig
    }
    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(
            mapOf(
                "Grafica_Facturas" to true
            )
        )
    }

    suspend fun fetchAndActivate(): Boolean {
        return try {
            remoteConfig.fetchAndActivate().await()
        } catch (_: Exception) {
            false
        }
    }

    fun getShowGraph(): Boolean {
        return remoteConfig.getBoolean("Grafica_Facturas")
    }
}
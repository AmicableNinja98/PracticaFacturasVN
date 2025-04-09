package com.example.data_retrofit.repository

import android.content.Context
import com.example.data_retrofit.services.SmartSolarService
import com.example.domain.use_details.UseDetails
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SmartSolarLocalService @Inject constructor(
    @ApplicationContext private val context: Context
) : SmartSolarService {

    override suspend fun getUseDetails(): UseDetails? {
        return try {
            val jsonString = context.assets.open("use_details.json")
                .bufferedReader()
                .use { it.readText() }

            val json = Json {
                ignoreUnknownKeys = true
            }

            json.decodeFromString<UseDetails>(jsonString)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
package com.example.data_retrofit.repository

import android.content.Context
import com.example.domain.appstrings.AppStrings
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import java.util.Locale
import javax.inject.Inject

class AppStringsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) {
    private var cachedStrings : AppStrings? = null

    suspend fun getAppStrings() : AppStrings{
        if(cachedStrings != null) return cachedStrings!!

        return try {
            val stringsFirestore = loadStringsFromFirestore()
            cachedStrings = stringsFirestore
            stringsFirestore
        }catch (_ : Exception){
            val backUp = loadFromJson()
            cachedStrings = backUp
            backUp
        }
    }

    private suspend fun loadStringsFromFirestore() : AppStrings{
        val snapshot = firestore.collection("appstrings").document("appstrings_${Locale.getDefault().language}").get().await()

        return snapshot.toObject(AppStrings::class.java) ?: throw IllegalStateException("Firestore returned null")
    }

    private fun loadFromJson() : AppStrings{
        val json = context.assets.open("app_strings_${Locale.getDefault().language}.json").bufferedReader().use { it.readText() }

        return Json.decodeFromString(json)
    }
}
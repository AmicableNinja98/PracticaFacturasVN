package com.example.data_retrofit.repository

import android.content.Context
import com.example.domain.appstrings.AppStrings
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.Locale
import javax.inject.Inject

class AppStringsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) {
    private var cachedStrings : AppStrings? = null

    fun getAppStrings() : Flow<AppStrings>{
        /*if(cachedStrings != null) return cachedStrings!!

        return try {
            loadStringsFromFirestore().collect {
                strings ->
                cachedStrings = strings
            }
            if(cachedStrings != null)
                cachedStrings!!
            else{
                val jsonStrings = loadFromJson()
                cachedStrings = jsonStrings
                jsonStrings
            }

        }catch (_ : Exception){
            val backUp = loadFromJson()
            cachedStrings = backUp
            backUp
        }*/
        return channelFlow {
            val firestoreFlow = loadStringsFromFirestore()

            val task = launch{
                firestoreFlow.collect { firestoreStrings ->
                    // Si alguno de los campos está vacío, asumimos que no hay datos en la base de datos.
                    if (firestoreStrings.filterStartDateTitle != "") {
                        cachedStrings = firestoreStrings
                        send(firestoreStrings)
                    } else {
                        if (cachedStrings == null) {
                            val backup = loadFromJson()
                            cachedStrings = backup
                            send(backup)
                        }
                    }
                }
            }

            awaitClose{
                task.cancel()
            }
        }
    }

    private fun loadStringsFromFirestore() : Flow<AppStrings>{
        return callbackFlow {
            val docRef = firestore.collection("appstrings").document("appstrings_${Locale.getDefault().language}")

            val listener : ListenerRegistration = docRef.addSnapshotListener { snapshot, error ->
                if(error != null){
                    close(error)
                    return@addSnapshotListener
                }
                val strings = snapshot?.toObject(AppStrings::class.java)
                if(strings != null){
                    trySend(strings).isSuccess
                }
            }

            awaitClose{
                listener.remove()
            }
        }
    }

    private fun loadFromJson() : AppStrings{
        val json = context.assets.open("app_strings_${Locale.getDefault().language}.json").bufferedReader().use { it.readText() }

        return Json.decodeFromString(json)
    }
}
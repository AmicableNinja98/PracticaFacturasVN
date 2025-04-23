package com.example.practicafacturas.home.usecase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel() {
    var useMockData by mutableStateOf(false)
        private set

    fun setUseMockDataValue(value : Boolean){
        useMockData = value
    }
}
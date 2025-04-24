package com.example.practicafacturas.home.usecase

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel() {
    var useMockData by mutableStateOf(false)
        private set

    val snackbarHostState by mutableStateOf(SnackbarHostState())

    fun onSwitchCheckedChange(value : Boolean){
        viewModelScope.launch {
            useMockData = value
            snackbarHostState.showSnackbar(message = if(useMockData) "Usando datos del Mock" else "Usando datos de la Api")
        }
    }
}
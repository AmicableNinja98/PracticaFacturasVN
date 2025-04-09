package com.example.core.ui.screens.smartSolar.usecase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data_retrofit.repository.SmartSolarLocalService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmartSolarScreenViewModel @Inject constructor(private val smartSolarLocalService: SmartSolarLocalService ) : ViewModel() {
    var state by mutableStateOf<SmartSolarScreenState>(SmartSolarScreenState.Loading)
        private set

    fun loadMockDetails() {
        viewModelScope.launch {
            state = SmartSolarScreenState.Loading
            val details = smartSolarLocalService.getUseDetails()
            if(details != null)
                state = SmartSolarScreenState.Success(details)
            else
                state = SmartSolarScreenState.NoData
        }
    }
}
package com.example.core.ui.screens.smartSolar.usecase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data_retrofit.repository.AppStringsRepository
import com.example.data_retrofit.repository.SmartSolarLocalService
import com.example.domain.appstrings.AppStrings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmartSolarScreenViewModel @Inject constructor(
    private val smartSolarLocalService: SmartSolarLocalService,
    private val appStringsRepository: AppStringsRepository
) : ViewModel() {
    var state by mutableStateOf<SmartSolarScreenState>(SmartSolarScreenState.Loading)
        private set

    val strings = MutableStateFlow<AppStrings?>(null)

    init {
        viewModelScope.launch {
            appStringsRepository.getAppStrings().collect {
                strings.value = it
            }
        }
    }

    fun loadMockDetails() {
        viewModelScope.launch {
            state = SmartSolarScreenState.Loading
            val details = smartSolarLocalService.getUseDetails()
            state = if (details != null)
                SmartSolarScreenState.Success(details)
            else
                SmartSolarScreenState.NoData
        }
    }
}
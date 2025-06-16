package com.example.practicafacturas.home.usecase

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data_retrofit.repository.AppStringsRepository
import com.example.domain.appstrings.AppStrings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val appStringsRepository: AppStringsRepository) : ViewModel() {
    var useMockData by mutableStateOf(false)
        private set

    val snackbarHostState by mutableStateOf(SnackbarHostState())

    val strings = MutableStateFlow<AppStrings?>(null)

    fun onSwitchCheckedChange(value : Boolean) {
        useMockData = value
    }

    fun showSnackbarMessage(messages : List<String>){
        viewModelScope.launch {
            snackbarHostState.showSnackbar(message = if(useMockData) messages.first() else messages[1])
        }
    }

    init {
        viewModelScope.launch {
            appStringsRepository.getAppStrings().collect {
                strings.value = it
            }
        }
    }
}
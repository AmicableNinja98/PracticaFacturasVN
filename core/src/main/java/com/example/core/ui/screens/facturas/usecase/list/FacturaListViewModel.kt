package com.example.core.ui.screens.facturas.usecase.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.firebase.RemoteConfigManager
import com.example.core.ui.screens.facturas.usecase.shared.FacturaSharedViewModel
import com.example.data_retrofit.repository.AppStringsRepository
import com.example.data_retrofit.repository.FacturaRepository
import com.example.domain.appstrings.AppStrings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacturaListViewModel @Inject constructor(
    private val facturaRepository: FacturaRepository,
    private val appStringsRepository: AppStringsRepository
) :
    ViewModel() {
    var state by mutableStateOf<FacturaListState>(FacturaListState.Loading)
        private set

    var showGraph by mutableStateOf(false)
        private set

    val strings = MutableStateFlow<AppStrings?>(null)

    init {
        viewModelScope.launch {
            appStringsRepository.getAppStrings().collect {
                strings.value = it
            }
            resetData()

            val value = RemoteConfigManager.getShowGraph()

            showGraph = value
        }
    }

    private fun resetData() {
        // Necesitamos hacer esto cada vez que se inicia el viewmodel para poder cambiar de fuente de datos en tiempo de ejecución
        viewModelScope.launch {
            facturaRepository.deleteAll()
            facturaRepository.resetIndexes()
        }
    }

    fun getFacturasFromApiOrDatabase(
        sharedViewModel: FacturaSharedViewModel,
        useJson: Boolean = false
    ) {
        viewModelScope.launch {
            state = FacturaListState.Loading
            if (sharedViewModel.areFiltersApplied())
                getFilteredFacturas(sharedViewModel)
            else
                getAllFacturas(useJson)
        }
    }

    private suspend fun getFilteredFacturas(sharedViewModel: FacturaSharedViewModel) {
        val facturas = sharedViewModel.getIds().mapNotNull { id ->
            facturaRepository.getFacturaById(id)
        }
        state = if (facturas.isNotEmpty())
            FacturaListState.Success(facturas)
        else
            FacturaListState.NoData
    }

    private suspend fun getAllFacturas(useMock: Boolean) {
        facturaRepository.getFacturasFromDatabase().collect { facturas ->
            if (facturas.isEmpty()) {
                facturaRepository.getDataFromApiAndInsertToDatabase(useMock)
                facturaRepository.getFacturasFromDatabase().collect { facturas ->
                    state = if (facturas.isEmpty())
                        FacturaListState.NoData
                    else
                        FacturaListState.Success(facturas)
                }
            } else
                state = FacturaListState.Success(facturas)
        }
    }
}
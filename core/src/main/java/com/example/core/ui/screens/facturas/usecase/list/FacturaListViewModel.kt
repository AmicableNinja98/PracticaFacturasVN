package com.example.core.ui.screens.facturas.usecase.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.firebase.RemoteConfigManager
import com.example.core.ui.screens.facturas.usecase.shared.FacturaSharedViewModel
import com.example.data_retrofit.repository.FacturaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacturaListViewModel @Inject constructor(private val facturaRepository: FacturaRepository) :
    ViewModel() {
    var state by mutableStateOf<FacturaListState>(FacturaListState.Loading)
        private set

    var showGraph by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            resetData()
            val success = RemoteConfigManager.fetchAndActivate()
            Log.d("RemoteConfig", "Fetch success: $success")
            val value = RemoteConfigManager.getShowGraph()
            Log.d("RemoteConfig", "Show graph: $value")
            showGraph = value
        }
    }

    private fun resetData(){
        // Necesitamos hacer esto cada vez que se inicia el viewmodel para poder cambiar de fuente de datos en tiempo de ejecución
        viewModelScope.launch {
            facturaRepository.deleteAll()
            facturaRepository.resetIndexes()
        }
    }

    fun getFacturasFromApiOrDatabase(sharedViewModel: FacturaSharedViewModel, useJson: Boolean = false) {
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
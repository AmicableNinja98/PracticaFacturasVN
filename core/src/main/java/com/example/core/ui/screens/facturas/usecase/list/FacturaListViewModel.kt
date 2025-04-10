package com.example.core.ui.screens.facturas.usecase.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.ui.screens.facturas.usecase.FacturaSharedViewModel
import com.example.data_retrofit.repository.FacturaRepository
import com.example.domain.factura.Factura
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacturaListViewModel @Inject constructor(private val facturaRepository: FacturaRepository) : ViewModel() {
    var state by mutableStateOf<FacturaListState>(FacturaListState.Loading)
        private set

    fun getFacturasFromApiOrDatabase(sharedViewModel: FacturaSharedViewModel) {
        viewModelScope.launch {
            state = FacturaListState.Loading
            if (sharedViewModel.getFilters())
                getFilteredFacturas(sharedViewModel)
            else
                getAllFacturas()
        }
    }

    private suspend fun getFilteredFacturas(sharedViewModel: FacturaSharedViewModel) {
        val facturas = mutableListOf<Factura>()
        sharedViewModel.getIds().forEach { id ->
            facturas.add(facturaRepository.getFacturaById(id)!!)
        }
        state = if (facturas.isNotEmpty())
            FacturaListState.Success(facturas)
        else
            FacturaListState.NoData
    }

    private suspend fun getAllFacturas() {
        facturaRepository.getFacturasFromDatabase().collect { facturas ->
            if (facturas.isEmpty()) {
                facturaRepository.getDataFromApiAndInsertToDatabase()
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

    fun sendIds(sharedViewModel: FacturaSharedViewModel) {
        if (state is FacturaListState.Success)
            sharedViewModel.setIds((state as FacturaListState.Success).facturas.map {
                it.id
            }.toMutableList())
    }
}
package com.example.core.ui.screens.facturas.list.usecase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data_retrofit.repository.FacturaRepository
import com.example.domain.factura.Factura
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacturaListViewModel @Inject constructor(private val facturaRepository: FacturaRepository) : ViewModel() {
    var state by mutableStateOf<FacturaListState>(FacturaListState.Loading)
        private set

    init {
        getFacturas()
    }

    private fun getFacturas(){
        /*viewModelScope.launch {
            state = FacturaListState.Loading
            facturaRepository.getFacturasFromDatabase().collect {
                facturas ->
                state = if(facturas.isEmpty())
                    FacturaListState.NoData
                else
                    FacturaListState.Success(facturas)
            }
        }*/
        state = FacturaListState.Success(
            listOf(
                Factura(
                    1,"Pendiente de pago",1.56,"07/02/2019"
                ),
                Factura(
                    2,"Pagada",25.14,"05/02/2019"
                )
            )
        )
    }
}
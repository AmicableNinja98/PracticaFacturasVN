package com.example.core.ui.screens.facturas.list.usecase

import com.example.domain.factura.Factura

sealed class FacturaListState{
    data object Loading : FacturaListState()
    data class Success(val facturas : List<Factura>) : FacturaListState()
    data object NoData : FacturaListState()
}
package com.example.core.ui.screens.facturas.usecase.filter

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.domain.factura.Factura

data class FacturaListFilterState(
    var fechaInicio : String? = null,
    var fechaFin : String? = null,
    var importeMin : Double = 0.0,
    var importeMax : Double = 0.0,
    var facturas : MutableList<Factura> = mutableStateListOf(),
    var estados : SnapshotStateList<EstadoFiltro> = mutableStateListOf(
        EstadoFiltro("Pagada", false),
        EstadoFiltro("Anuladas", false),
        EstadoFiltro("Cuota fija", false),
        EstadoFiltro("Pendiente de pago", false),
        EstadoFiltro("Plan de pago", false)
    ),

    var filtroFechaAplicado : Boolean = false,
    var filtroImporteAplicado : Boolean = false,
    var filtroEstadoAplicado : Boolean = false,

    var sinDatos : Boolean = false
)

data class EstadoFiltro(
    val nombre: String,
    val seleccionado: Boolean
)
package com.example.core.ui.screens.facturas.usecase.shared

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.core.ui.screens.facturas.usecase.filter.EstadoFiltro

data class FacturaSharedState(
    var idList: MutableList<Int> = mutableListOf(),

    var filtersApplied: Boolean = false,

    var filterImporteMin: Double = 0.0,

    var filterImporteMax: Double = 0.0,

    var filterEstados : SnapshotStateList<EstadoFiltro> = mutableStateListOf(
        EstadoFiltro("Pagada", false),
        EstadoFiltro("Anulada", false),
        EstadoFiltro("Cuota fija", false),
        EstadoFiltro("Pendiente de pago", false),
        EstadoFiltro("Plan de pago", false)
    ),

    var filterFechaMin : String? = null,

    var filterFechaMax : String? = null,
)
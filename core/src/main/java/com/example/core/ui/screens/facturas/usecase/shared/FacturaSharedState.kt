package com.example.core.ui.screens.facturas.usecase.shared

import androidx.compose.runtime.mutableStateMapOf

data class FacturaSharedState(
    var idList: MutableList<Int> = mutableListOf(),

    var jsonUsed : Boolean = false,

    var filtersApplied: Boolean = false,

    var filterImporteMin: Double = 0.0,

    var filterImporteMax: Double = 0.0,

    var filterEstados : MutableMap<String, Boolean> = mutableStateMapOf<String, Boolean>(),

    var filterFechaMin : String? = null,

    var filterFechaMax : String? = null,
)
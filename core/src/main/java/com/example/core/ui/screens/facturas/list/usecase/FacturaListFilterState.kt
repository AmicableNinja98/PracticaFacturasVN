package com.example.core.ui.screens.facturas.list.usecase

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import com.example.domain.factura.Factura

data class FacturaListFilterState(
    var fechaInicio : String? = null,
    var fechaFin : String? = null,
    var importeMin : Double = 0.0,
    var importeMax : Double = 0.0,
    var facturas : MutableList<Factura> = mutableStateListOf(),
    var estados : MutableMap<String, Boolean> = mutableStateMapOf<String, Boolean>(
        "Pagada" to false,
        "Anuladas" to false,
        "Cuota fija" to false,
        "Pendiente de pago" to false,
        "Plan de pago" to false
    ),

    var filtroFechaAplicado : Boolean = false,
    var filtroImporteAplicado : Boolean = false,
    var filtroEstadoAplicado : Boolean = false,

    var sinDatos : Boolean = false
)
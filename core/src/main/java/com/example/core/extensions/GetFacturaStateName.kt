package com.example.core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import com.example.core.R
import com.example.domain.appstrings.AppStrings

@Composable
fun getFacturaStateName(estado : String, strings: State<AppStrings?>): String {
    return when (estado) {
        "Pagada" -> strings.value?.facturaDescStatePagada ?: stringResource(R.string.factura_descState_pagada)
        "Anulada" -> strings.value?.facturaDescStateAnulada ?: stringResource(R.string.factura_descState_anulada)
        "Cuota fija" -> strings.value?.facturaDescStateCuotaFija ?: stringResource(R.string.factura_descState_cuota_fija)
        "Pendiente de pago" -> strings.value?.facturaDescStatePendienteDePago ?: stringResource(R.string.factura_descState_pendiente_de_pago)
        "Plan de pago" -> strings.value?.facturaDescStatePlanDePago ?: stringResource(R.string.factura_descState_plan_de_pago)
        else -> ""
    }
}
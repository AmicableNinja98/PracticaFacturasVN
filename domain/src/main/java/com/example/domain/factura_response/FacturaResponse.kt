package com.example.domain.factura_response

import com.example.domain.factura.Factura
import kotlinx.serialization.Serializable

@Serializable
data class FacturaResponse(
    val numFacturas : Int,
    val facturas : List<Factura>
)
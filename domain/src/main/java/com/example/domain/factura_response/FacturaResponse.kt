package com.example.domain.factura_response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FacturaResponse(
    @SerialName("numFacturas")
    val numFacturas: Int,

    @SerialName("facturas")
    val facturas: List<FacturaApi>
)

@Serializable
data class FacturaApi(
    @SerialName(value = "descEstado")
    val descEstado : String,
    @SerialName(value = "importeOrdenacion")
    val importeOrdenacion : Double,
    @SerialName(value = "fecha")
    val fecha : String
)
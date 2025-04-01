package com.example.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Data class que almacena los datos de las facturas recogidos de la api.
 * @param id -> Id para usar como clave primaria de la base de datos
 * @param descEstado -> Descripción del estado de la factura.
 * @param importeOrdenacion -> Importe de ordenación de la factura.
 * @param fecha -> Fecha de creación de la factura
 */
@Serializable
@Entity
data class Factura(
    @PrimaryKey
    var id : Int? = null,
    val descEstado : String,
    val importeOrdenacion : Double,
    val fecha : String
)
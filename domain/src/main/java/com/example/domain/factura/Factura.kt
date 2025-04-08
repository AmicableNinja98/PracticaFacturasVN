package com.example.domain.factura

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class que almacena los datos de las facturas recogidos de la base de datos.
 * @param id -> Id para usar como clave primaria de la base de datos
 * @param descEstado -> Descripción del estado de la factura.
 * @param importeOrdenacion -> Importe de ordenación de la factura.
 * @param fecha -> Fecha de creación de la factura
 */
@Entity
data class Factura(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val descEstado : String,
    val importeOrdenacion : Double,
    val fecha : String
)
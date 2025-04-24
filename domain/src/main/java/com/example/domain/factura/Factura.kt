package com.example.domain.factura

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Factura(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val descEstado : String,
    val importeOrdenacion : Double,
    val fecha : String
)
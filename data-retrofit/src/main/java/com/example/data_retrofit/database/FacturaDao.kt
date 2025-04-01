package com.example.data_retrofit.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.domain.Factura
import kotlinx.coroutines.flow.Flow

@Dao
interface FacturaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFactura(factura: Factura)

    @Delete
    suspend fun deleteFactura(factura: Factura)

    @Update
    suspend fun updateFactura(factura: Factura)

    @Query("SELECT * FROM Factura")
    fun getAllFacturas() : Flow<List<Factura>>

    @Query("SELECT * FROM Factura WHERE id = :id")
    suspend fun getFacturaById(id : Int) : Factura?
}
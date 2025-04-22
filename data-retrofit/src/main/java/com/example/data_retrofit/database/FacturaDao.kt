package com.example.data_retrofit.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.factura.Factura
import kotlinx.coroutines.flow.Flow

@Dao
interface FacturaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFactura(factura: Factura)

    @Query("SELECT * FROM Factura")
    fun getAllFacturas() : Flow<List<Factura>>

    @Query("SELECT * FROM Factura WHERE id = :id")
    suspend fun getFacturaById(id : Int) : Factura?

    @Query("SELECT MAX(id) FROM Factura")
    suspend fun getLastFacturaId() : Int

    @Query("DELETE FROM Factura")
    suspend fun deleteAll()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'factura'")
    suspend fun deletePrimaryKeyIndex()
}
package com.example.data_retrofit.repository

import com.example.data_retrofit.database.FacturaDao
import com.example.data_retrofit.services.FacturaApiService
import com.example.domain.factura.Factura
import javax.inject.Inject

/**
 * Repositorio que se encarga de enlazar los viewModel de la entidad Factura con la api y la base de datos local.
 * @param facturaApiService -> Api a usar para recoger los datos.
 * @param facturaDao -> Dao que usaremos para guardar los datos recogidos de la api en la base de datos local.
 */
class FacturaRepository @Inject constructor(private val facturaApiService: FacturaApiService,private val facturaDao: FacturaDao) {
    /**
     * Recoge las facturas de la api.
     */
    suspend fun getFacturasFromApi() = facturaApiService.getFacturas()

    /**
     * Recoge las facturas de la base de datos.
     */
    fun getFacturasFromDatabase() = facturaDao.getAllFacturas()

    /**
     * Inserta una factura en la base de datos.
     * @param factura -> Factura a insertar.
     */
    suspend fun insertFactura(factura: Factura){
        factura.id = getLastFacturaId()
        facturaDao.insertFactura(factura)
    }

    /**
     * Elimina una factura en la base de datos.
     * @param factura -> Factura a eliminar.
     */
    suspend fun deleteFactura(factura: Factura) = facturaDao.deleteFactura(factura)

    /**
     * Actualiza una factura en la base de datos.
     * @param factura -> Factura a actualizar.
     */
    suspend fun updateFactura(factura: Factura) = facturaDao.updateFactura(factura)

    /**
     * Recoge una factura de la base de datos.
     * @param id -> Id de la factura a recoger.
     */
    suspend fun getFacturaById(id : Int) = facturaDao.getFacturaById(id)

    /**
     * Recoge el último id de una factura registrado en la base de datos.
     */
    suspend fun getLastFacturaId() = facturaDao.getLastFacturaId()
}
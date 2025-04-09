package com.example.data_retrofit.repository

import com.example.data_retrofit.database.FacturaDao
import com.example.data_retrofit.services.FacturaApiService
import com.example.data_retrofit.services.RetromockService
import com.example.domain.factura.Factura
import com.example.domain.factura_response.FacturaApi
import javax.inject.Inject

/**
 * Repositorio que se encarga de enlazar los viewModel de la entidad Factura con la api y la base de datos local.
 * @param facturaApiService -> Servicio de donde se recogen los datos.
 * @param facturaDao -> Dao que usaremos para guardar los datos recogidos de la api en la base de datos local.
 */
class FacturaRepository @Inject constructor(private val facturaApiService: FacturaApiService,private val retromockService: RetromockService,private val facturaDao: FacturaDao) {

    suspend fun getFacturasFromApi() = facturaApiService.getFacturas()

    suspend fun getFacturasFromMock() = retromockService.getFacturasMock()

    suspend fun getDataFromApiAndInsertToDatabase(){
        var response = getFacturasFromApi()
        if(response.isSuccessful){
            val body = response.body()
            if(body != null && body.numFacturas > 0 && body.facturas.isNotEmpty())
                body.facturas.forEach {
                        factura ->
                    insertFactura(factura)
                }
        }
    }

    fun getFacturasFromDatabase() = facturaDao.getAllFacturas()

    /**
     * Inserta una factura en la base de datos.
     * @param facturaApi -> Factura recogida de la api a insertar.
     */
    suspend fun insertFactura(facturaApi: FacturaApi){
        facturaDao.insertFactura(Factura(
            descEstado = facturaApi.descEstado,
            importeOrdenacion = facturaApi.importeOrdenacion,
            fecha = facturaApi.fecha
        ))
    }

    suspend fun getFacturaById(id : Int) = facturaDao.getFacturaById(id)
}
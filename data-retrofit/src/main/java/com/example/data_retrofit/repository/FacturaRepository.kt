package com.example.data_retrofit.repository

import com.example.data_retrofit.database.FacturaDao
import com.example.data_retrofit.services.FacturaApiService
import com.example.data_retrofit.services.MockApi
import com.example.data_retrofit.services.RealApi
import com.example.domain.factura.Factura
import com.example.domain.factura_response.FacturaApi
import javax.inject.Inject

/**
 * Repositorio que se encarga de enlazar los viewModel de la entidad Factura con la api y la base de datos local.
 */
class FacturaRepository @Inject constructor(
    @RealApi private val facturaApiService: FacturaApiService,
    @MockApi private val mockFacturaApiService: FacturaApiService,
    private val facturaDao: FacturaDao
) {
    private suspend fun getFacturasFromApi() = facturaApiService.getFacturas()

    private suspend fun getFacturasFromMock() = mockFacturaApiService.getMockFacturas()

    suspend fun getDataFromApiAndInsertToDatabase(useMock : Boolean = false) {
        var response = if(useMock) getFacturasFromMock() else getFacturasFromApi()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null && body.numFacturas > 0 && body.facturas.isNotEmpty())
                body.facturas.forEach { factura ->
                    insertFactura(factura)
                }
        }
    }

    fun getFacturasFromDatabase() = facturaDao.getAllFacturas()

    suspend fun insertFactura(facturaApi: FacturaApi) {
        facturaDao.insertFactura(
            Factura(
                descEstado = facturaApi.descEstado,
                importeOrdenacion = facturaApi.importeOrdenacion,
                fecha = facturaApi.fecha,
                energiaConsumida = facturaApi.energiaConsumida
            )
        )
    }

    suspend fun getFacturaById(id: Int) = facturaDao.getFacturaById(id)

    suspend fun deleteAll() = facturaDao.deleteAll()

    suspend fun resetIndexes() = facturaDao.deletePrimaryKeyIndex()
}
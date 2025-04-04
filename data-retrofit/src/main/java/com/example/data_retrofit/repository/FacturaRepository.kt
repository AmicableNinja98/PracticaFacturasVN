package com.example.data_retrofit.repository

import androidx.compose.runtime.mutableStateMapOf
import com.example.data_retrofit.database.FacturaDao
import com.example.data_retrofit.services.FacturaApiService
import com.example.domain.factura.Factura
import com.example.domain.factura_response.FacturaApi
import javax.inject.Inject

/**
 * Repositorio que se encarga de enlazar los viewModel de la entidad Factura con la api y la base de datos local.
 * @param facturaApiService -> Api a usar para recoger los datos.
 * @param facturaDao -> Dao que usaremos para guardar los datos recogidos de la api en la base de datos local.
 */
class FacturaRepository @Inject constructor(private val facturaApiService: FacturaApiService,private val facturaDao: FacturaDao) {

    /*init {
        runBlocking {
            val response = getFacturasFromApi()
            if(response.isSuccessful){
                val body = response.body()
                if(body != null && *//*body.numFacturas > 0 &&*//* body.facturas.isNotEmpty())
                    body.facturas.forEach {
                        factura -> insertFactura(factura)
                    }
            }
        }
    }*/

    /*init {
        runBlocking {
            val response = getFacturasFromApi()
            if(response.facturas.isNotEmpty())
                response.facturas.forEach {
                    factura -> insertFactura(factura)
                }
        }
    }*/

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
     * @param facturaApi -> Factura recogida de la api a insertar.
     */
    suspend fun insertFactura(facturaApi: FacturaApi){
        facturaDao.insertFactura(Factura(
            id = getLastFacturaId(),
            descEstado = facturaApi.descEstado,
            importeOrdenacion = facturaApi.importeOrdenacion,
            fecha = facturaApi.fecha
        ))
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
    //suspend fun getFacturaById(id : Int) = facturaDao.getFacturaById(id)

    /**
     * Recoge el último id de una factura registrado en la base de datos.
     */
    suspend fun getLastFacturaId() = facturaDao.getLastFacturaId()

    companion object{
        private var idList : MutableList<Int> = mutableListOf()
        private var filtersApplied = false
        private var facturas = listOf(
            Factura(
                1,"Pendiente de pago",1.56,"07/02/2019"
            ),
            Factura(
                2,"Pagada",22.69,"05/02/2019"
            ),
            Factura(
                3,"Pagada",25.14,"08/01/2019"
            ),
            Factura(
                4,"Pendiente de pago",12.84,"07/12/2019"
            ),
            Factura(
                5,"Pagada",35.15,"16/11/2018"
            ),
            Factura(
                6,"Pagada",18.27,"05/10/2018"
            ),
            Factura(
                7,"Pendiente de pago",61.17,"05/09/2018"
            ),
            Factura(
                8,"Pagada",37.18,"07/08/2018"
            ),
        )

        fun getFacturas() = facturas

        fun getFacturaById(id : Int) : Factura? = facturas.firstOrNull { it.id == id }

        fun getIds() : MutableList<Int> = idList

        fun setIds(ids : MutableList<Int>){
            idList = ids
        }

        fun setFiltersApplied(value : Boolean){
            filtersApplied = value
        }

        fun getFiltersApplied() = filtersApplied
    }
}
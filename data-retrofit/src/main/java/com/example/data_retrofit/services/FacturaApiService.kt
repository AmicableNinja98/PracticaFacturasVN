package com.example.data_retrofit.services

import com.example.domain.Factura
import retrofit2.http.GET

/**
 * Interfaz que se encargará de hacer las peticiones correspondientes a la api.
 */
interface FacturaApiService {
    /**
     * Realiza una petición a la api para obtener todos los datos de las facturas disponibles.
     */
    @GET("facturas")
    suspend fun getFacturas() : List<Factura>
}
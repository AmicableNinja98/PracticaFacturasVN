package com.example.data_retrofit.services

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.example.domain.factura_response.FacturaResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Interfaz que se encargará de hacer las peticiones correspondientes a la api.
 */
interface FacturaApiService {
    @GET("facturas")
    suspend fun getFacturas() : Response<FacturaResponse>

    @Mock
    @MockResponse(body = "facturas.json")
    suspend fun getMockFacturas() : Response<FacturaResponse>
}
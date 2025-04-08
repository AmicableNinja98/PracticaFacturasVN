package com.example.data_retrofit.services

import co.infinum.retromock.meta.Mock
import com.example.domain.factura_response.FacturaResponse
import retrofit2.Response
import retrofit2.http.GET

interface RetromockService {
    @Mock
    @GET("facturas")
    suspend fun getFacturasMock() : Response<FacturaResponse>
}
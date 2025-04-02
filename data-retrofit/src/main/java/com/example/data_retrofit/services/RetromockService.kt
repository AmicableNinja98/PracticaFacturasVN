package com.example.data_retrofit.services

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.example.domain.factura_response.FacturaResponse
import retrofit2.Response
import retrofit2.http.GET

interface RetromockService {
    @Mock
    @MockResponse(body = "retromock/response.json")
    @GET("facturas")
    suspend fun getFacturas() : Response<FacturaResponse>
}
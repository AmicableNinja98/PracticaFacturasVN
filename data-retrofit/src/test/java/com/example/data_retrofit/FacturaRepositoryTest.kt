package com.example.data_retrofit

import co.infinum.retromock.Retromock
import com.example.data_retrofit.database.FacturaDao
import com.example.data_retrofit.repository.FacturaRepository
import com.example.data_retrofit.services.AssetsBodyFactory
import com.example.data_retrofit.services.FacturaApiService
import com.example.data_retrofit.services.MockApi
import com.example.data_retrofit.services.RealApi
import com.example.domain.factura.Factura
import com.example.domain.factura_response.FacturaApi
import com.example.domain.factura_response.FacturaResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response
import retrofit2.Retrofit

class FacturaRepositoryTest {
    private lateinit var facturaDao: FacturaDao
    private lateinit var retromock : Retromock
    @RealApi
    private lateinit var facturaApiService: FacturaApiService
    @MockApi
    private lateinit var mockFacturaApiService: FacturaApiService
    private lateinit var repository: FacturaRepository

    @BeforeEach
    fun setup() {
        facturaDao = mock()
        facturaApiService = mock()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()

        retromock = Retromock.Builder()
            .retrofit(retrofit)
            .defaultBodyFactory(AssetsBodyFactory("src/test/resources/"))
            .build()

        mockFacturaApiService = retromock.create(FacturaApiService::class.java)

        repository = FacturaRepository(
            facturaApiService = mock(),
            mockFacturaApiService = mockFacturaApiService,
            facturaDao = facturaDao
        )
        repository = FacturaRepository(facturaApiService,mockFacturaApiService, facturaDao)
    }

    @Test
    fun `insertFactura calls DAO with mapped Factura`() =
        runTest {
            val facturaApi = FacturaApi("Emitida", 99.9, "2024-04-01")

            repository.insertFactura(facturaApi)

            verify(facturaDao).insertFactura(
                check {
                    Assertions.assertEquals("Emitida", it.descEstado)
                    Assertions.assertEquals(99.9, it.importeOrdenacion)
                    Assertions.assertEquals("2024-04-01", it.fecha)
                }
            )
        }

    @Test
    fun `getFacturaById calls DAO and returns result`() =
        runTest {
            val factura = Factura(
                id = 1,
                descEstado = "Pagada",
                importeOrdenacion = 12.0,
                fecha = "2024-02-02"
            )
            whenever(facturaDao.getFacturaById(1)).thenReturn(factura)

            val result = repository.getFacturaById(1)

            Assertions.assertEquals(factura, result)
        }

    @Test
    fun `getDataFromApiAndInsertToDatabase saves data when response is successful`() =
        runTest {
            val facturasApi = listOf(
                FacturaApi("Emitida", 100.0, "2024-01-01"),
                FacturaApi("Pagada", 200.0, "2024-02-02")
            )
            val response = FacturaResponse(facturas = facturasApi, numFacturas = facturasApi.size)

            whenever(facturaApiService.getFacturas()).thenReturn(Response.success(response))

            repository.getDataFromApiAndInsertToDatabase()

            verify(facturaDao, times(2)).insertFactura(any())
        }

    @Test
    fun `getDataFromApiAndInsertToDatabase saves data when response is successful using mock`() =
        runTest {
            repository.getDataFromApiAndInsertToDatabase(true)

            verify(facturaDao, times(11)).insertFactura(any())
        }

    @Test
    fun `getFacturasFromDatabase returns flow from DAO`() =
        runTest {
            val list = listOf(
                Factura(
                    id = 1,
                    descEstado = "Pagada",
                    importeOrdenacion = 45.5,
                    fecha = "2024-01-01"
                )
            )
            whenever(facturaDao.getAllFacturas()).thenReturn(flowOf(list))

            val flow = repository.getFacturasFromDatabase()
            val result = flow.first()

            Assertions.assertEquals(list, result)
        }

    @Test
    fun `getDataFromApiAndInsertToDatabase does nothing when response is not successful`() =
        runTest {
            whenever(facturaApiService.getFacturas()).thenReturn(
                Response.error(
                    500,
                    "Error".toResponseBody()
                )
            )

            repository.getDataFromApiAndInsertToDatabase()

            verify(facturaDao, never()).insertFactura(any())
        }

    @Test
    fun `getDataFromApiAndInsertToDatabase does nothing when body is null`() =
        runTest {
            whenever(facturaApiService.getFacturas()).thenReturn(
                Response.success(null)
            )

            repository.getDataFromApiAndInsertToDatabase()

            verify(facturaDao, never()).insertFactura(any())
        }

    @Test
    fun `getDataFromApiAndInsertToDatabase does nothing when numFacturas = 0`() =
        runTest {
            whenever(facturaApiService.getFacturas()).thenReturn(
                Response.success(FacturaResponse(facturas = listOf(), numFacturas = 0))
            )

            repository.getDataFromApiAndInsertToDatabase()

            verify(facturaDao,never()).insertFactura(any())
        }

    @Test
    fun `getDataFromApiAndInsertToDatabase does nothing when facturas is empty`() =
        runTest {
            whenever(facturaApiService.getFacturas()).thenReturn(
                Response.success(FacturaResponse(facturas = listOf(), numFacturas = 2))
            )

            repository.getDataFromApiAndInsertToDatabase()

            verify(facturaDao,never()).insertFactura(any())
        }

    @Test
    fun `deleteAll calls DAO deleteAll`() = runTest {
        repository.deleteAll()
        verify(facturaDao).deleteAll()
    }

    @Test
    fun `resetIndexes calls DAO deletePrimaryKeyIndex`() = runTest {
        repository.resetIndexes()
        verify(facturaDao).deletePrimaryKeyIndex()
    }
}

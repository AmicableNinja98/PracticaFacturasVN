package com.example.data_retrofit

import android.content.Context
import com.example.data_retrofit.database.FacturaDao
import com.example.data_retrofit.repository.FacturaRepository
import com.example.data_retrofit.services.FacturaApiService
import com.example.domain.factura.Factura
import com.example.domain.factura_response.FacturaApi
import com.example.domain.factura_response.FacturaResponse
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response

class FacturaRepositoryTest {
    private lateinit var facturaDao: FacturaDao
    private lateinit var facturaApiService: FacturaApiService
    private lateinit var context: Context
    private lateinit var repository: FacturaRepository

    @Before
    fun setup() {
        facturaDao = mock()
        facturaApiService = mock()
        context = mock()
        repository = FacturaRepository(facturaApiService, facturaDao,context)
    }

    @Test
    fun `insertFactura calls DAO with mapped Factura`() =
        runTest {
            val facturaApi = FacturaApi("Emitida", 99.9, "2024-04-01")

            repository.insertFactura(facturaApi)

            verify(facturaDao).insertFactura(
                check {
                    assertEquals("Emitida", it.descEstado)
                    assertEquals(99.9, it.importeOrdenacion)
                    assertEquals("2024-04-01", it.fecha)
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

            assertEquals(factura, result)
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

            assertEquals(list, result)
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
}

package com.example.data_retrofit

import android.content.Context
import android.content.res.AssetManager
import com.example.data_retrofit.repository.SmartSolarLocalService
import com.example.domain.use_details.UseDetails
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.ByteArrayInputStream

class SmartSolarLocalServiceTest {

    private lateinit var context: Context
    private lateinit var assets: AssetManager
    private lateinit var service: SmartSolarLocalService

    @Before
    fun setup() {
        context = mock()
        assets = mock()

        whenever(context.assets).thenReturn(assets)

        service = SmartSolarLocalService(context)
    }

    @Test
    fun `getUseDetails returns UseDetails when JSON is valid`() {
        runBlocking {
            val fakeJson = """
            {
              "cau": "123456",
              "estado": "activo",
              "tipo": "hogar",
              "compensacion": "si",
              "potencia": "5kW"
            }
        """.trimIndent()

            val inputStream = ByteArrayInputStream(fakeJson.toByteArray())
            whenever(assets.open("use_details.json")).thenReturn(inputStream)

            val result = service.getUseDetails()

            val expected = UseDetails(
                codigo = "123456",
                estado = "activo",
                tipo = "hogar",
                compensacion = "si",
                potencia = "5kW"
            )

            assertEquals(expected, result)
        }
    }

    @Test
    fun `getUseDetails returns null when exception occurs`() {
        runBlocking {
            whenever(assets.open("use_details.json")).thenThrow(RuntimeException("File not found"))

            val result = service.getUseDetails()

            assertNull(result)
        }
    }
}

package com.example.data_retrofit

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data_retrofit.database.FacturaDao
import com.example.data_retrofit.database.FacturaDatabase
import com.example.domain.factura.Factura
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class FacturaDaoTest {
    private lateinit var database : FacturaDatabase
    private lateinit var dao : FacturaDao

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FacturaDatabase::class.java,
        ).allowMainThreadQueries()
            .build()

        dao = database.getFacturaDao()
    }

    @Test
    fun `insert and get Factura`(){
        runBlocking {
            val factura = Factura(
                descEstado = "Pagada",
                importeOrdenacion = 23.30,
                fecha = "20/02/2024"
            )
            dao.insertFactura(factura)
            var facturaFromDb = dao.getAllFacturas().first().firstOrNull()
            assertEquals(factura.descEstado, facturaFromDb?.descEstado)
            assertEquals(factura.fecha, facturaFromDb?.fecha)
            assertEquals(factura.importeOrdenacion, facturaFromDb?.importeOrdenacion)
        }
    }

    @Test
    fun `insert factura and getItById`(){
        runBlocking {
            val factura = Factura(
                descEstado = "Pagada",
                importeOrdenacion = 23.30,
                fecha = "20/02/2024"
            )
            dao.insertFactura(factura)
            val lastId = dao.getLastFacturaId()
            val facturaFromDb = dao.getFacturaById(lastId)

            assertEquals(factura.descEstado,facturaFromDb?.descEstado)
            assertEquals(factura.fecha,facturaFromDb?.fecha)
            assertEquals(factura.importeOrdenacion,facturaFromDb?.importeOrdenacion)
        }
    }

    @After
    fun closeDatabase(){
        database.close()
    }
}
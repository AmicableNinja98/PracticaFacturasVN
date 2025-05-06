package com.example.core

import com.example.core.extensions.toMillis
import com.example.core.ui.screens.facturas.usecase.filter.FacturaListFilterViewModel
import com.example.core.ui.screens.facturas.usecase.shared.FacturaSharedViewModel
import com.example.data_retrofit.repository.FacturaRepository
import com.example.domain.factura.Factura
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.text.SimpleDateFormat

class FacturaListFilterViewModelTest {
    private lateinit var viewModel: FacturaListFilterViewModel
    private lateinit var facturaRepository: FacturaRepository
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        facturaRepository = mock()
        Dispatchers.setMain(testDispatcher)
        viewModel = FacturaListFilterViewModel(facturaRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getFacturas sets initial state without filters`() = runTest {
        val sharedViewModel = FacturaSharedViewModel().apply {
            setAreFiltersApplied(false)
        }
        val facturas = listOf(
            Factura(1, "Pagada", 100.0, "01/01/2024"),
            Factura(2, "Pagada", 300.0, "01/02/2024")
        )

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturas))

        viewModel.getFacturas(sharedViewModel)
        advanceUntilIdle()

        with(viewModel.state) {
            assertEquals(facturas.minOf {
                factura -> factura.importeOrdenacion
            }, importeMin)
            assertEquals(facturas.maxOf {
                    factura -> factura.importeOrdenacion
            }, importeMax)
            assertEquals(facturas, this.facturas)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getFacturas sets initial state with previousFilterApplied if some filters were applied before`() = runTest {
        val facturasTest = listOf(Factura(1, "Pagada", 100.0, "01/01/2024"))
        val sharedViewModel = FacturaSharedViewModel().apply {
            setAreFiltersApplied(true)
            setFechaMin(null)
            setFechaMax("10/01/2024")
            setImporteMax(120.0)
        }

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturasTest))

        viewModel.getFacturas(sharedViewModel)
        advanceUntilIdle()

        with(viewModel.state) {
            assertEquals(facturasTest,facturas)
            assertEquals(importeMax,120.0)
            assertEquals(fechaFin,"10/01/2024")
            assertFalse(estados.any {
                it.seleccionado
            })
            assertFalse(filtroEstadoAplicado)
            assertTrue(filtroImporteAplicado)
            assertTrue(filtroFechaAplicado)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getFacturas sets initial state with previousFilterApplied if filters were applied before`() = runTest {
        val facturasTest = listOf(Factura(1, "Pagada", 100.0, "01/01/2024"))
        val sharedViewModel = FacturaSharedViewModel().apply {
            setAreFiltersApplied(true)
            setImporteMin(100.0)
        }

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturasTest))

        viewModel.getFacturas(sharedViewModel)
        advanceUntilIdle()

        with(viewModel.state) {
            assertEquals(facturasTest,facturas)
            assertEquals(importeMin,100.0)
            assertFalse(estados.any {
                it.seleccionado
            })
            assertFalse(filtroEstadoAplicado)
            assertTrue(filtroImporteAplicado)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getFacturas sets initial state with previousFilterApplied if other filters were applied before`() = runTest {
        val facturasTest = listOf(Factura(1, "Pagada", 100.0, "01/01/2024"))
        val sharedViewModel = FacturaSharedViewModel().apply {
            setAreFiltersApplied(true)
            setFechaMin("01/01/2024")
            setFechaMax("10/10/2024")
            setImporteMin(100.0)
            setImporteMax(100.0)
            setEstado(0,true)
            setEstado(2,true)
        }

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturasTest))

        viewModel.getFacturas(sharedViewModel)
        advanceUntilIdle()

        with(viewModel.state) {
            assertEquals(facturasTest,facturas)
            assertTrue(estados.any{
                it.seleccionado
            })
            assertTrue(filtroEstadoAplicado)
            assertFalse(filtroImporteAplicado)
            assertTrue(filtroFechaAplicado)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `filtroFechaAplicado is true if both dates are set`() = runTest{
        val facturasTest = listOf(Factura(1, "Pagada", 100.0, "01/01/2024"))
        val sharedViewModel = FacturaSharedViewModel().apply {
            setAreFiltersApplied(true)
            setFechaMin("01/01/2023")
            setFechaMax("12/02/2024")
        }

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturasTest))

        viewModel.getFacturas(sharedViewModel)
        advanceUntilIdle()

        with(viewModel.state) {
            assertEquals(fechaInicio,"01/01/2023")
            assertEquals(fechaFin,"12/02/2024")
            assertTrue(filtroFechaAplicado)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getFacturas does nothing if database is empty`() = runTest{
        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(emptyList()))
        val sharedViewModel = FacturaSharedViewModel().apply {
            setAreFiltersApplied(false)
        }

        viewModel.getFacturas(sharedViewModel)
        advanceUntilIdle()
        with(viewModel.state) {
            assertTrue(facturas.isEmpty())
            assertEquals(importeMin,0.0)
            assertEquals(importeMax,0.0)
            assertEquals(fechaInicio,null)
            assertEquals(fechaFin,null)
            assertTrue(estados.all {
                it.seleccionado == false
            })
            assertEquals(filtroFechaAplicado,false)
            assertEquals(filtroImporteAplicado,false)
            assertEquals(filtroEstadoAplicado,false)
            assertEquals(sinDatosAlFiltrar,false)
        }
    }

    @Test
    fun `onCheckedChange toggles estado selection`() = runTest {
        viewModel.onCheckedChange(0)

        assertTrue(viewModel.state.estados[0].seleccionado)
        assertTrue(viewModel.state.filtroEstadoAplicado)
    }

    @Test
    fun `onCheckedChange toggles filtroEstadoSeleccionado to false if none are selected`() = runTest{
        viewModel.onCheckedChange(0)
        viewModel.onCheckedChange(0)

        assertFalse(viewModel.state.estados[0].seleccionado)
        assertFalse(viewModel.state.filtroEstadoAplicado)
    }

    @Test
    fun `onDateChanged update fecha and flag`() = runTest {
        val start = SimpleDateFormat("dd/MM/yyyy").parse("01/01/2024")?.time
        val end = SimpleDateFormat("dd/MM/yyyy").parse("10/01/2024")?.time

        viewModel.onDateChanged(start,isStartDate = true)
        viewModel.onDateChanged(end)

        assertEquals("01/01/2024", viewModel.state.fechaInicio)
        assertEquals("10/01/2024", viewModel.state.fechaFin)
        assertTrue(viewModel.state.filtroFechaAplicado)
    }

    @Test
    fun `onDateChanged does nothing if date is null`() = runTest {
        viewModel.onDateChanged(null,isStartDate = true)
        viewModel.onDateChanged(null)

        assertEquals(null, viewModel.state.fechaInicio)
        assertEquals(null, viewModel.state.fechaFin)
        assertFalse(viewModel.state.filtroFechaAplicado)
    }

    @Test
    fun `onSliderValueChange updates importe and flag`() = runTest {
        viewModel.onSliderValueChange(100f..300f)

        with(viewModel.state) {
            assertEquals(100.0, importeMin)
            assertEquals(300.0, importeMax)
            assertTrue(filtroImporteAplicado)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onApplyFiltersClick filters facturas with some filters applied and updates sharedViewModel`() = runTest {
        val facturas = listOf(
            Factura(1, "Pagada", 100.0, "01/04/2024"),
            Factura(2, "Pagada", 300.0, "01/06/2024")
        )
        val sharedViewModel = FacturaSharedViewModel().apply {
            setAreFiltersApplied(false)
        }

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturas))

        viewModel.getFacturas(sharedViewModel)
        advanceUntilIdle()

        viewModel.onSliderValueChange(90f..150f)
        viewModel.onCheckedChange(0)
        viewModel.onApplyFiltersClick(sharedViewModel)

        val filtered = viewModel.state.facturas
        assertEquals(1, filtered.size)
        assertEquals(1, filtered.first().id)
        assertEquals(sharedViewModel.areFiltersApplied(),true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onApplyFiltersClick filters facturas with only end date filter applied and updates sharedViewModel`() = runTest {
        val facturas = listOf(
            Factura(1, "Pagada", 100.0, "01/04/2024"),
            Factura(2, "Pagada", 300.0, "01/06/2024")
        )
        val sharedViewModel = FacturaSharedViewModel().apply {
            setAreFiltersApplied(false)
        }

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturas))

        viewModel.getFacturas(sharedViewModel)
        advanceUntilIdle()

        viewModel.state.fechaInicio = null
        viewModel.onDateChanged("01/05/2024".toMillis())
        viewModel.onApplyFiltersClick(sharedViewModel)

        val filtered = viewModel.state.facturas
        assertEquals(1, filtered.size)
        assertEquals(1, filtered.first().id)
        assertEquals(sharedViewModel.areFiltersApplied(),true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onApplyFiltersClick filters facturas with only start date filter applied and updates sharedViewModel`() = runTest{
        val facturas = listOf(
            Factura(1, "Pagada", 100.0, "01/06/2024"),
            Factura(2, "Pagada", 300.0, "01/01/2023")
        )
        val sharedViewModel = FacturaSharedViewModel().apply {
            setAreFiltersApplied(false)
        }

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturas))

        viewModel.getFacturas(sharedViewModel)
        advanceUntilIdle()

        viewModel.onDateChanged("01/05/2024".toMillis(),isStartDate = true)
        viewModel.onApplyFiltersClick(sharedViewModel)

        val filtered = viewModel.state.facturas
        assertEquals(1, filtered.size)
        assertTrue(sharedViewModel.areFiltersApplied())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun`onApplyFiltersClick does not filter facturas by date if facturas dont have date`() = runTest {
        val facturas = listOf(
            Factura(1, "Pagada", 100.0, ""),
            Factura(2, "Pagada", 300.0, "")
        )
        val sharedViewModel = FacturaSharedViewModel().apply {
            setAreFiltersApplied(false)
        }

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturas))

        viewModel.getFacturas(sharedViewModel)
        advanceUntilIdle()

        viewModel.onDateChanged("01/11/2023".toMillis(),isStartDate = true)
        viewModel.onApplyFiltersClick(sharedViewModel)

        val filtered = viewModel.state.facturas
        assertEquals(facturas.size, filtered.size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onApplyFiltersClick sets sinDatos if no matches`() = runTest {
        val facturas = listOf(
            Factura(1, "Pagada", 100.0, "01/01/2024")
        )
        val sharedViewModel = FacturaSharedViewModel().apply {
            setAreFiltersApplied(false)
        }

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturas))

        viewModel.getFacturas(sharedViewModel)
        advanceUntilIdle()

        viewModel.onSliderValueChange(200f..300f)
        viewModel.onCheckedChange(2)
        viewModel.onApplyFiltersClick(sharedViewModel)

        assertTrue(viewModel.state.sinDatosAlFiltrar)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onFiltersReset resets filters and clears sharedViewModel filters`() = runTest {
        val facturas = listOf(
            Factura(1, "Pagada", 100.0, "01/01/2024"),
            Factura(2, "Pagada", 300.0, "01/02/2024")
        )
        val sharedViewModel = FacturaSharedViewModel().apply {
            setAreFiltersApplied(false)
        }

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturas))

        viewModel.getFacturas(sharedViewModel)
        advanceUntilIdle()

        viewModel.onSliderValueChange(100f..300f)
        viewModel.onDateChanged(SimpleDateFormat("dd/MM/yyyy").parse("01/01/2024")?.time,isStartDate = true)
        viewModel.onCheckedChange(0)
        viewModel.onFiltersReset(sharedViewModel)

        val state = viewModel.state
        assertFalse(state.filtroFechaAplicado)
        assertFalse(state.filtroEstadoAplicado)
        assertFalse(state.filtroImporteAplicado)
        assertEquals(sharedViewModel.areFiltersApplied(),false)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getImporteMinFromFacturas and getImporteMaxFromFacturas throw NoSuchElementException when facturas is empty `() = runTest{
        val sharedViewModel = FacturaSharedViewModel().apply {
            setAreFiltersApplied(false)
        }

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(emptyList()))
        viewModel.getFacturas(sharedViewModel)
        advanceUntilIdle()

        assertEquals(0.0,viewModel.state.importeMin)
        assertEquals(0.0,viewModel.state.importeMax)
    }
}
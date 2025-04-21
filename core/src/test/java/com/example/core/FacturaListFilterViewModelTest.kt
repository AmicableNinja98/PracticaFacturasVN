package com.example.core

import com.example.core.ui.screens.facturas.usecase.filter.EstadoFiltro
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.text.SimpleDateFormat

class FacturaListFilterViewModelTest {
    private lateinit var viewModel: FacturaListFilterViewModel
    private lateinit var facturaRepository: FacturaRepository
    private lateinit var sharedViewModel: FacturaSharedViewModel
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        facturaRepository = mock()
        Dispatchers.setMain(testDispatcher)
        sharedViewModel = mock()
        viewModel = FacturaListFilterViewModel(facturaRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getFacturasFromRepository sets initial state without filters`() = runTest {
        val facturas = listOf(
            Factura(1, "Pagada", 100.0, "01/01/2024"),
            Factura(2, "Pagada", 300.0, "01/02/2024")
        )

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturas))
        whenever(sharedViewModel.getFilters()).thenReturn(false)

        viewModel.getFacturasFromRepository(sharedViewModel)
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

    @Test
    fun `onCheckedChange toggles estado selection`() = runTest {
        val estado = EstadoFiltro("Pendiente de pago", false)
        viewModel.state.estados.add(estado)

        viewModel.onCheckedChange(0)

        assertTrue(viewModel.state.estados[0].seleccionado)
        assertTrue(viewModel.state.filtroEstadoAplicado)
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
    fun `onApplyFiltersClick filters facturas and updates sharedViewModel`() = runTest {
        val facturas = listOf(
            Factura(1, "Pagada", 100.0, "01/01/2024"),
            Factura(2, "Pagada", 300.0, "01/02/2024")
        )

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturas))
        whenever(sharedViewModel.getFilters()).thenReturn(false)

        viewModel.getFacturasFromRepository(sharedViewModel)
        advanceUntilIdle()

        viewModel.onSliderValueChange(90f..150f)
        viewModel.onApplyFiltersClick(sharedViewModel)

        val filtered = viewModel.state.facturas
        assertEquals(1, filtered.size)
        assertEquals(1, filtered.first().id)
        verify(sharedViewModel).setFilters(true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onApplyFiltersClick sets sinDatos if no matches`() = runTest {
        val facturas = listOf(
            Factura(1, "Pagada", 100.0, "01/01/2024")
        )

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturas))
        whenever(sharedViewModel.getFilters()).thenReturn(false)

        viewModel.getFacturasFromRepository(sharedViewModel)
        advanceUntilIdle()

        viewModel.onSliderValueChange(200f..300f)
        viewModel.onApplyFiltersClick(sharedViewModel)

        assertTrue(viewModel.state.sinDatos)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onFiltersReset resets filters and clears sharedViewModel filters`() = runTest {
        val facturas = listOf(
            Factura(1, "Pagada", 100.0, "01/01/2024"),
            Factura(2, "Pagada", 300.0, "01/02/2024")
        )

        whenever(facturaRepository.getFacturasFromDatabase()).thenReturn(flowOf(facturas))
        whenever(sharedViewModel.getFilters()).thenReturn(false)

        viewModel.getFacturasFromRepository(sharedViewModel)
        advanceUntilIdle()

        viewModel.onSliderValueChange(100f..300f)
        viewModel.onDateChanged(SimpleDateFormat("dd/MM/yyyy").parse("01/01/2024")?.time,isStartDate = true)
        viewModel.onCheckedChange(0)
        viewModel.onFiltersReset(sharedViewModel)

        val state = viewModel.state
        assertFalse(state.filtroFechaAplicado)
        assertFalse(state.filtroEstadoAplicado)
        assertFalse(state.filtroImporteAplicado)
        verify(sharedViewModel).setFilters(false)
    }
}
package com.example.core

import com.example.core.ui.screens.facturas.usecase.list.FacturaListState
import com.example.core.ui.screens.facturas.usecase.list.FacturaListViewModel
import com.example.core.ui.screens.facturas.usecase.shared.FacturaSharedViewModel
import com.example.data_retrofit.repository.FacturaRepository
import com.example.domain.factura.Factura
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class FacturaListViewModelTest {
    private lateinit var repository: FacturaRepository
    private lateinit var sharedViewModel: FacturaSharedViewModel
    private lateinit var viewModel: FacturaListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        sharedViewModel = mock()
        viewModel = FacturaListViewModel(repository)
    }

    @Test
    fun `getFacturasFromApiOrDatabase with filters returns Success`() = runTest {
        val factura = Factura(id = 1, descEstado = "Pagada", importeOrdenacion = 12.0, fecha = "2024-02-02")
        whenever(sharedViewModel.getFilters()).thenReturn(true)
        whenever(sharedViewModel.getIds()).thenReturn(listOf(1))
        whenever(repository.getFacturaById(1)).thenReturn(factura)

        viewModel.getFacturasFromApiOrDatabase(sharedViewModel)

        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.state is FacturaListState.Success)
        assertEquals(listOf(factura), (viewModel.state as FacturaListState.Success).facturas)
    }

    @Test
    fun `getFacturasFromApiOrDatabase with filters returns NoData`() = runTest {
        whenever(sharedViewModel.getFilters()).thenReturn(true)
        whenever(sharedViewModel.getIds()).thenReturn(listOf(1))
        whenever(repository.getFacturaById(1)).thenReturn(null)

        viewModel.getFacturasFromApiOrDatabase(sharedViewModel)

        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.state is FacturaListState.NoData)
    }

    @Test
    fun `getFacturasFromApiOrDatabase without filters returns Success from DB`() = runTest {
        val facturaList = listOf(Factura(id = 1, descEstado = "Pagada", importeOrdenacion = 12.0, fecha = "2024-02-02"))
        whenever(sharedViewModel.getFilters()).thenReturn(false)
        whenever(repository.getFacturasFromDatabase()).thenReturn(flowOf(facturaList))

        viewModel.getFacturasFromApiOrDatabase(sharedViewModel)

        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.state is FacturaListState.Success)
        assertEquals(facturaList, (viewModel.state as FacturaListState.Success).facturas)
    }

    @Test
    fun `getFacturasFromApiOrDatabase loads from API when DB is empty`() = runTest {
        val facturaList = listOf(Factura(id = 1, descEstado = "Pagada", importeOrdenacion = 12.0, fecha = "2024-02-02"))

        whenever(sharedViewModel.getFilters()).thenReturn(false)
        whenever(repository.getFacturasFromDatabase())
            .thenReturn(flowOf(emptyList()))
            .thenReturn(flowOf(facturaList))

        whenever(repository.getDataFromApiAndInsertToDatabase()).thenReturn(Unit)

        viewModel.getFacturasFromApiOrDatabase(sharedViewModel)

        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.state is FacturaListState.Success)
        assertEquals(facturaList, (viewModel.state as FacturaListState.Success).facturas)
    }

    @Test
    fun `getFacturasFromApiOrDatabase returns NoData when all empty`() = runTest {
        whenever(sharedViewModel.getFilters()).thenReturn(false)
        whenever(repository.getFacturasFromDatabase())
            .thenReturn(flowOf(emptyList()))
            .thenReturn(flowOf(emptyList()))

        whenever(repository.getDataFromApiAndInsertToDatabase()).thenReturn(Unit)

        viewModel.getFacturasFromApiOrDatabase(sharedViewModel)

        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.state is FacturaListState.NoData)
    }

    @Test
    fun `sendIds sets IDs in sharedViewModel when state is Success`() {
        val facturas = listOf(
            Factura(id = 1, descEstado = "Pagada", importeOrdenacion = 12.0, fecha = "2024-02-02"),
            Factura(id = 2, descEstado = "Pagada", importeOrdenacion = 40.0, fecha = "2024-02-01")
        )
        viewModel.setTestState(FacturaListState.Success(facturas))

        viewModel.sendIds(sharedViewModel)

        verify(sharedViewModel).setIds(mutableListOf(1, 2))
    }

    @Test
    fun `sendIds does nothing when state is not Success`() {
        viewModel.setTestState(FacturaListState.NoData)

        viewModel.sendIds(sharedViewModel)

        verify(sharedViewModel, never()).setIds(any())
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }
}
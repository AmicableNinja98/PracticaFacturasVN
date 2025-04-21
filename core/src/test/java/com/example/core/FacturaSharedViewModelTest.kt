package com.example.core

import com.example.core.ui.screens.facturas.usecase.shared.FacturaSharedViewModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FacturaSharedViewModelTest {
    private lateinit var viewModel: FacturaSharedViewModel

    @BeforeEach
    fun setup() {
        viewModel = FacturaSharedViewModel()
    }

    @Test
    fun `setIds updates id list`() {
        val ids = listOf(1, 2, 3)
        viewModel.setIds(ids)
        assertEquals(ids, viewModel.getIds())
    }

    @Test
    fun `setFilters updates filtersApplied`() {
        viewModel.setFilters(true)
        assertTrue(viewModel.getFilters())

        viewModel.setFilters(false)
        assertFalse(viewModel.getFilters())
    }

    @Test
    fun `setImporteMin updates filterImporteMin`() {
        viewModel.setImporteMin(10.5)
        assertEquals(10.5, viewModel.getImporteMin())
    }

    @Test
    fun `setImporteMax updates filterImporteMax`() {
        viewModel.setImporteMax(99.9)
        assertEquals(99.9, viewModel.getImporteMax())
    }

    @Test
    fun `setFechaMin updates filterFechaMin`() {
        val date = "2024-01-01"
        viewModel.setFechaMin(date)
        assertEquals(date, viewModel.getFechaMin())
    }

    @Test
    fun `setFechaMax updates filterFechaMax`() {
        val date = "2024-12-31"
        viewModel.setFechaMax(date)
        assertEquals(date, viewModel.getFechaMax())
    }

    @Test
    fun `setEstado updates filterEstados`() {
        viewModel.setEstado("Pagada", true)
        assertTrue(viewModel.getEstados()["Pagada"] == true)

        viewModel.setEstado("Pendiente", false)
        assertTrue(viewModel.getEstados()["Pendiente"] == false)
    }
}

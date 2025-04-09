package com.example.core.ui.screens.facturas.list.usecase.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.extensions.toLocalDateOrNull
import com.example.core.ui.screens.facturas.list.usecase.FacturaSharedViewModel
import com.example.data_retrofit.repository.FacturaRepository
import com.example.domain.factura.Factura
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FacturaListFilterViewModel @Inject constructor(val facturaRepository: FacturaRepository) : ViewModel() {
    var state by mutableStateOf<FacturaListFilterState>(FacturaListFilterState())
        private set

    private var facturasOriginal = mutableListOf<Factura>()

    private var format = "dd/MM/yyyy"

    fun getFacturasFromRepository() {
        viewModelScope.launch {
            facturaRepository.getFacturasFromDatabase().collect {
                facturas ->
                if(facturas.isNotEmpty()) {
                    updateStateAfterInit(facturas.toMutableList())
                    facturasOriginal = facturas.toMutableList()
                }
            }
        }
    }

    private fun updateStateAfterInit(facturas : MutableList<Factura>){
        state = state.copy(
            facturas = facturas,
            importeMin = facturas.minOf { it.importeOrdenacion },
            importeMax = facturas.maxOf { it.importeOrdenacion }
        )
    }

    fun onCheckedChange(key: String) {
        val estados = state.estados
        estados.put(key, !estados.getValue(key))
        state = state.copy(estados = estados)
        state = if (state.estados.values.any {
                it == true
            })
            state.copy(filtroEstadoAplicado = true)
        else
            state.copy(filtroEstadoAplicado = false)

    }

    fun onStartDateChanged(date: Long?) {
        if (date != null) {
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            state = state.copy(fechaInicio = dateFormat.format(date), filtroFechaAplicado = true)
        }
    }

    fun onEndDateChanged(date: Long?) {
        if (date != null) {
            val dateFormat = SimpleDateFormat(format,Locale.getDefault())
            state = state.copy(fechaFin = dateFormat.format(date), filtroFechaAplicado = true)
        }
    }

    fun onSliderValueChange(values: ClosedFloatingPointRange<Float>) {
        state = state.copy(
            importeMin = values.start.toDouble(),
            importeMax = values.endInclusive.toDouble(),
            filtroImporteAplicado = true
        )
    }

    fun onFiltersReset(sharedViewModel: FacturaSharedViewModel) {
        resetState(sharedViewModel)
    }

    private fun resetState(sharedViewModel: FacturaSharedViewModel){
        val estados = state.estados
        estados.keys.forEach {
            state.estados.put(it, false)
        }
        state = state.copy(
            estados = estados,
            fechaInicio = null,
            fechaFin = null,
            importeMin = facturasOriginal.minOf { it.importeOrdenacion },
            importeMax = facturasOriginal.maxOf { it.importeOrdenacion },
            filtroImporteAplicado = false,
            filtroFechaAplicado = false,
            filtroEstadoAplicado = false,
            sinDatos = false
        )
        sharedViewModel.setFilters(false)
    }

    fun onApplyFiltersClick(sharedViewModel: FacturaSharedViewModel) {
        if (state.filtroEstadoAplicado || state.filtroImporteAplicado || state.filtroFechaAplicado)
            applyFiltersAndUpdateState(sharedViewModel)
        else
            sendAllIdsToRepository(sharedViewModel)
    }

    private fun applyFiltersAndUpdateState(sharedViewModel: FacturaSharedViewModel){
        val facturasFiltradas = applyFiltersToList()
        if(facturasFiltradas.isNotEmpty()){
            state = state.copy(facturas = facturasFiltradas, sinDatos = false)

            sharedViewModel.setFilters(true)
            sharedViewModel.setIds(state.facturas.map {
                it.id
            }.toMutableList())
        }
        else
            state = state.copy(sinDatos = true)
    }

    private fun applyFiltersToList(): MutableList<Factura> {
        return facturasOriginal.filter { factura ->
            val facturaDate = factura.fecha.replace("/", "-").toLocalDateOrNull()
            val startDate = if (state.fechaInicio != null) state.fechaInicio!!.replace(
                "/",
                "-"
            ).toLocalDateOrNull() else "01-01-2000".toLocalDateOrNull()
            val endDate = if (state.fechaFin != null) state.fechaFin!!.replace(
                "/",
                "-"
            ).toLocalDateOrNull() else /*LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ofPattern(format))*/
                "31-12-2025".toLocalDateOrNull()

            (if (state.filtroFechaAplicado) facturaDate!! >= startDate && facturaDate <= endDate
            else true) &&
                    (if (state.filtroImporteAplicado) factura.importeOrdenacion >= state.importeMin && factura.importeOrdenacion <= state.importeMax else true) &&
                    (if (state.filtroEstadoAplicado) factura.descEstado == state.estados.keys.firstOrNull { key ->
                        key == factura.descEstado && state.estados.getValue(key) == true
                    } else true)
        }.toMutableList()
    }

    private fun sendAllIdsToRepository(sharedViewModel: FacturaSharedViewModel) {
        sharedViewModel.setIds(facturasOriginal.map {
            it.id
        }.toMutableList())
    }
}
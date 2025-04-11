package com.example.core.ui.screens.facturas.usecase.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.extensions.toLocalDateOrNull
import com.example.core.ui.screens.facturas.usecase.shared.FacturaSharedViewModel
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

    fun getFacturasFromRepository(sharedViewModel: FacturaSharedViewModel) {
        viewModelScope.launch {
            facturaRepository.getFacturasFromDatabase().collect {
                facturas ->
                if(facturas.isNotEmpty()) {
                    updateStateAfterInit(facturas.toMutableList(),sharedViewModel)
                    facturasOriginal = facturas.toMutableList()
                }
            }
        }
    }

    private fun updateStateAfterInit(facturas : MutableList<Factura>,sharedViewModel: FacturaSharedViewModel){
        state = if(sharedViewModel.getFilters()){
            state.copy(
                facturas = facturas,
                importeMin = sharedViewModel.getImporteMin(),
                importeMax = sharedViewModel.getImporteMax(),
                fechaInicio = sharedViewModel.getFechaMin(),
                fechaFin = sharedViewModel.getFechaMax(),
                estados = mapToEstadoFiltroList(sharedViewModel.getEstados()),
                filtroFechaAplicado = sharedViewModel.getFechaMin() != null || sharedViewModel.getFechaMax() != null,
                filtroImporteAplicado = sharedViewModel.getImporteMin() != facturas.minOf { it.importeOrdenacion } || sharedViewModel.getImporteMax() != facturas.maxOf { it.importeOrdenacion },
                filtroEstadoAplicado = sharedViewModel.getEstados().any {
                    it.value == true
                }
            )
        } else
            state.copy(
                facturas = facturas,
                importeMin = facturas.minOf { it.importeOrdenacion },
                importeMax = facturas.maxOf { it.importeOrdenacion }
            )
    }

    fun onCheckedChange(index : Int) {
        val estado = state.estados[index]
        state.estados[index] = estado.copy(seleccionado = !estado.seleccionado)
        state = if (state.estados.any {
                it.seleccionado == true
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
        estados.forEachIndexed {
            index,estado -> estados[index] = estado.copy(seleccionado = false)
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
        applyFiltersAndUpdateState(sharedViewModel)
    }

    private fun applyFiltersAndUpdateState(sharedViewModel: FacturaSharedViewModel){
        val facturasFiltradas = applyFiltersToList()
        if(facturasFiltradas.isNotEmpty()){
            state = state.copy(facturas = facturasFiltradas, sinDatos = false)
            updateSharedViewModel(sharedViewModel)
        }
        else
            state = state.copy(sinDatos = true)
    }

    private fun applyFiltersToList(): MutableList<Factura> {
        val startDate = if (state.fechaInicio != null) state.fechaInicio!!.replace(
            "/",
            "-"
        ).toLocalDateOrNull() else "01-01-2000".toLocalDateOrNull()
        val endDate = if (state.fechaFin != null) state.fechaFin!!.replace(
            "/",
            "-"
        ).toLocalDateOrNull() else /*LocalDate.parse(LocalDate.now().toString(), DateTimeFormatter.ofPattern(format))*/
            "31-12-2025".toLocalDateOrNull()

        val estadosSeleccionados = state.estados.filter {
            it.seleccionado
        }.map { it.nombre }

        return facturasOriginal.filter { factura ->
            val facturaDate = factura.fecha.replace("/", "-").toLocalDateOrNull()
            (if (state.filtroFechaAplicado) facturaDate!! >= startDate && facturaDate <= endDate
            else true) &&
                    (if (state.filtroImporteAplicado) factura.importeOrdenacion >= state.importeMin && factura.importeOrdenacion <= state.importeMax else true) &&
                    (if (state.filtroEstadoAplicado) estadosSeleccionados.contains(factura.descEstado) else true)
        }.toMutableList()
    }

    private fun updateSharedViewModel(sharedViewModel: FacturaSharedViewModel){
        sharedViewModel.setFilters(true)

        sharedViewModel.setIds(state.facturas.map {
            it.id
        }.toMutableList())

        sharedViewModel.setFechaMin(state.fechaInicio)
        sharedViewModel.setFechaMax(state.fechaFin)
        sharedViewModel.setImporteMin(state.importeMin)
        sharedViewModel.setImporteMax(state.importeMax)
        state.estados.forEach {
            estado -> sharedViewModel.setEstado(estado.nombre,estado.seleccionado)
        }
    }

    private fun mapToEstadoFiltroList(map: Map<String, Boolean>): SnapshotStateList<EstadoFiltro> {
        val lista = mutableStateListOf<EstadoFiltro>()
        map.forEach { (key, value) ->
            lista.add(EstadoFiltro(nombre = key, seleccionado = value))
        }
        return lista
    }
}
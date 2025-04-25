package com.example.core.ui.screens.facturas.usecase.filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.extensions.toLocalDateOrNull
import com.example.core.ui.screens.facturas.usecase.shared.FacturaSharedViewModel
import com.example.data_retrofit.repository.FacturaRepository
import com.example.domain.factura.Factura
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class FacturaListFilterViewModel @Inject constructor(val facturaRepository: FacturaRepository) : ViewModel() {
    var state by mutableStateOf<FacturaListFilterState>(FacturaListFilterState())
        private set

    private var facturasOriginalBaseDeDatos = mutableListOf<Factura>()

    private var formatToApplyToDates = "dd/MM/yyyy"

    fun getFacturas(sharedViewModel: FacturaSharedViewModel) = getFacturasFromRepository(sharedViewModel)

    private fun getFacturasFromRepository(sharedViewModel: FacturaSharedViewModel) =
        viewModelScope.launch {
            facturaRepository.getFacturasFromDatabase().collect { facturas ->
                if (facturas.isNotEmpty()) {
                    updateStateAfterInit(facturas.toMutableList(), sharedViewModel)
                    facturasOriginalBaseDeDatos = facturas.toMutableList()
                }
            }
        }

    private fun updateStateAfterInit(
        facturas: MutableList<Factura>,
        sharedViewModel: FacturaSharedViewModel
    ) {
        state = if (sharedViewModel.areFiltersApplied()) {
            state.copy(
                facturas = facturas,
                importeMin = sharedViewModel.getImporteMin(),
                importeMax = sharedViewModel.getImporteMax(),
                fechaInicio = sharedViewModel.getFechaMin(),
                fechaFin = sharedViewModel.getFechaMax(),
                estados = sharedViewModel.getEstados(),
                filtroFechaAplicado = sharedViewModel.getFechaMin() != null || sharedViewModel.getFechaMax() != null,
                filtroImporteAplicado = sharedViewModel.getImporteMin() != getImporteMinFromFacturas(facturas) || sharedViewModel.getImporteMax() != getImporteMaxFromFacturas(facturas),
                filtroEstadoAplicado = sharedViewModel.getEstados().any {
                    it.seleccionado == true
                }
            )
        } else
            state.copy(
                facturas = facturas,
                importeMin = getImporteMinFromFacturas(facturas),
                importeMax = getImporteMaxFromFacturas(facturas)
            )
    }

    fun onCheckedChange(index: Int) {
        val estado = state.estados[index]
        state.estados[index] = estado.copy(seleccionado = !estado.seleccionado)
        state = if (state.estados.any {
                it.seleccionado == true
            })
            state.copy(filtroEstadoAplicado = true)
        else
            state.copy(filtroEstadoAplicado = false)
    }

    fun onDateChanged(date: Long?, isStartDate: Boolean = false) {
        if (date != null) {
            val dateFormat = SimpleDateFormat(formatToApplyToDates, Locale.getDefault())
            state =
                if (isStartDate)
                    state.copy(fechaInicio = dateFormat.format(date), filtroFechaAplicado = true)
                else
                    state.copy(fechaFin = dateFormat.format(date), filtroFechaAplicado = true)
        }
    }

    fun onSliderValueChange(values: ClosedFloatingPointRange<Float>) {
        state = state.copy(
            importeMin = values.start.toDouble(),
            importeMax = values.endInclusive.toDouble(),
            filtroImporteAplicado = true
        )
    }

    fun onFiltersReset(sharedViewModel: FacturaSharedViewModel) = resetState(sharedViewModel)

    private fun resetState(sharedViewModel: FacturaSharedViewModel) {
        state.estados.forEachIndexed { index, estado ->
            state.estados[index] = estado.copy(seleccionado = false)
        }
        state = state.copy(
            fechaInicio = null,
            fechaFin = null,
            importeMin = getImporteMinFromFacturas(facturasOriginalBaseDeDatos),
            importeMax = getImporteMaxFromFacturas(facturasOriginalBaseDeDatos),
            filtroImporteAplicado = false,
            filtroFechaAplicado = false,
            filtroEstadoAplicado = false,
            sinDatosAlFiltrar = false
        )
        sharedViewModel.setAreFiltersApplied(false)
    }

    fun onApplyFiltersClick(sharedViewModel: FacturaSharedViewModel) = applyFiltersAndUpdateState(sharedViewModel)

    private fun applyFiltersAndUpdateState(sharedViewModel: FacturaSharedViewModel) {
        val facturasFiltradas = applyFiltersToList()
        if (facturasFiltradas.isNotEmpty()) {
            state = state.copy(facturas = facturasFiltradas.toMutableList(), sinDatosAlFiltrar = false)
            updateSharedViewModel(sharedViewModel)
        } else
            state = state.copy(sinDatosAlFiltrar = true)
    }

    private fun applyFiltersToList(): List<Factura> {
        val startDate = state.fechaInicio?.replace(
            "/",
            "-"
        )?.toLocalDateOrNull() ?: LocalDate.MIN
        val endDate = state.fechaFin?.replace(
            "/",
            "-"
        )?.toLocalDateOrNull() ?: LocalDate.MAX

        val estadosSeleccionados = state.estados.filter {
            it.seleccionado
        }.map { it.nombre }

        // Siempre filtramos a partir de la copia de todas las facturas que obtuvimos de la base de datos
        return facturasOriginalBaseDeDatos.filter { factura ->
            val facturaDate = factura.fecha.replace("/", "-").toLocalDateOrNull()

            val fechaValida = if (state.filtroFechaAplicado && facturaDate != null) {
                (startDate == null || !facturaDate.isBefore(startDate)) &&
                        (endDate == null || !facturaDate.isAfter(endDate))
            } else true

            val importeValido = if (state.filtroImporteAplicado) {
                factura.importeOrdenacion in state.importeMin..ceil(state.importeMax)
            } else true

            val estadoValido = if (state.filtroEstadoAplicado) {
                factura.descEstado in estadosSeleccionados
            } else true

            fechaValida && importeValido && estadoValido
        }
    }

    private fun updateSharedViewModel(sharedViewModel: FacturaSharedViewModel) {
        sharedViewModel.setAreFiltersApplied(true)

        sharedViewModel.setIds(state.facturas.map {
            it.id
        }.toMutableList())

        sharedViewModel.setFechaMin(state.fechaInicio)
        sharedViewModel.setFechaMax(state.fechaFin)
        sharedViewModel.setImporteMin(state.importeMin)
        sharedViewModel.setImporteMax(state.importeMax)
        state.estados.forEachIndexed { index,estado ->
            sharedViewModel.setEstado(index,estado.seleccionado)
        }
    }

    private fun getImporteMinFromFacturas(facturas : List<Factura>) = facturas.minOf { it.importeOrdenacion }

    private fun getImporteMaxFromFacturas(facturas : List<Factura>) = facturas.maxOf { it.importeOrdenacion }
}
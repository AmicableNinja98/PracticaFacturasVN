package com.example.core.ui.screens.facturas.usecase.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.core.ui.screens.facturas.usecase.filter.EstadoFiltro
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FacturaSharedViewModel @Inject constructor() : ViewModel() {
    var state by mutableStateOf<FacturaSharedState>(FacturaSharedState())
        private set
    fun getIds() : List<Int> = state.idList

    fun setIds(ids : List<Int>){
        state.idList = ids.toMutableList()
    }

    fun areFiltersApplied() : Boolean = state.filtersApplied

    fun setAreFiltersApplied(value : Boolean){
        state.filtersApplied = value
    }

    fun getImporteMin() : Double = state.filterImporteMin

    fun setImporteMin(value : Double){
        state.filterImporteMin = value
    }

    fun getImporteMax() : Double = state.filterImporteMax

    fun setImporteMax(value : Double){
        state.filterImporteMax = value
    }

    fun getFechaMin() : String? = state.filterFechaMin

    fun setFechaMin(value : String?){
        state.filterFechaMin = value
    }

    fun getFechaMax() : String? = state.filterFechaMax

    fun setFechaMax(value : String?){
        state.filterFechaMax = value
    }

    fun getEstados() : SnapshotStateList<EstadoFiltro> = state.filterEstados

    fun setEstado(index : Int,value : Boolean){
        val estado = state.filterEstados[index]
        state.filterEstados[index] = estado.copy(seleccionado = value)
    }
}
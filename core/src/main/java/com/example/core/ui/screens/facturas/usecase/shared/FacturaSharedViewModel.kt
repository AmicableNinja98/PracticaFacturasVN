package com.example.core.ui.screens.facturas.usecase.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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

    fun getIsJsonUsed() = state.jsonUsed

    fun setIsMockUsed(value: Boolean){
        state.jsonUsed = value
    }

    fun getFilters() : Boolean = state.filtersApplied

    fun setFilters(value : Boolean){
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

    fun getEstados() : Map<String, Boolean> = state.filterEstados

    fun setEstado(key : String,value: Boolean){
        state.filterEstados[key] = value
    }
}
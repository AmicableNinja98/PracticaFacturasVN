package com.example.core.ui.screens.facturas.usecase.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FacturaSharedViewModel @Inject constructor() : ViewModel() {
    private var idList by mutableStateOf<List<Int>>(emptyList())

    private var filtersApplied by mutableStateOf(false)

    private var filterImporteMin by mutableDoubleStateOf(0.0)

    private var filterImporteMax by mutableDoubleStateOf(0.0)

    private var filterEstados = mutableStateMapOf<String, Boolean>()

    private var filterFechaMin by mutableStateOf<String?>("")

    private var filterFechaMax by mutableStateOf<String?>("")

    fun getIds() : List<Int> = idList

    fun setIds(ids : List<Int>){
        idList = ids
    }

    fun getFilters() : Boolean = filtersApplied

    fun setFilters(value : Boolean){
        filtersApplied = value
    }

    fun getImporteMin() : Double = filterImporteMin

    fun setImporteMin(value : Double){
        filterImporteMin = value
    }

    fun getImporteMax() : Double = filterImporteMax

    fun setImporteMax(value : Double){
        filterImporteMax = value
    }

    fun getFechaMin() : String? = filterFechaMin

    fun setFechaMin(value : String?){
        filterFechaMin = value
    }

    fun getFechaMax() : String? = filterFechaMax

    fun setFechaMax(value : String?){
        filterFechaMax = value
    }

    fun getEstados() : Map<String, Boolean> = filterEstados

    fun setEstado(key : String,value: Boolean){
        filterEstados[key] = value
    }
}
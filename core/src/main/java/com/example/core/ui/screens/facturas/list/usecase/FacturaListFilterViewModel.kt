package com.example.core.ui.screens.facturas.list.usecase

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.data_retrofit.repository.FacturaRepository
import com.example.domain.factura.Factura
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FacturaListFilterViewModel @Inject constructor() : ViewModel() {
    var state by mutableStateOf<FacturaListFilterState>(FacturaListFilterState())
        private set

    private var facturasOriginal = mutableListOf<Factura>()

    init {
        facturasOriginal = FacturaRepository.getFacturas().toMutableList()
    }

    fun getFacturasFromRepo() {
        val facturas = mutableStateListOf<Factura>()
        FacturaRepository.getIds().forEach {
            facturas.add(FacturaRepository.getFacturaById(it)!!)
        }
        if(facturas.isNotEmpty())
            state = state.copy(facturas = facturas,sinDatos = false, importeMin = facturas.minOf { it.importeOrdenacion } ,importeMax = facturas.maxOf { it.importeOrdenacion })
        else if(FacturaRepository.getFiltersApplied())
            state = state.copy(facturas = facturasOriginal,sinDatos = false)
        //resetFilters()
    }

    /*private fun resetFilters(){
        val estados = state.estados
        estados.forEach {
            state.estados.put(it.key,false)
        }
        state = state.copy(
            estados = estados,
        )
    }*/

    fun onCheckedChange(key : String){
        val estados = state.estados
        estados.put(key,!estados.getValue(key))
        state = state.copy(estados = estados)
        state = if(state.estados.values.any {
                it == true
            })
            state.copy(filtroEstadoAplicado = true)
        else
            state.copy(filtroEstadoAplicado = false)

    }

    fun onSliderValueChange(newValue: ClosedFloatingPointRange<Float>){
        state = state.copy(sliderValue = newValue, filtroImporteAplicado = true)
    }

    fun onFiltersReset(){
        if(state.filtroEstadoAplicado){
            val estados = state.estados
            estados.keys.forEach {
                state.estados.put(it,false)
            }
            state = state.copy(estados = estados)
        }
        FacturaRepository.setFiltersApplied(false)
        FacturaRepository.setIds(facturasOriginal.map {
            it.id
        }.toMutableList())
    }

    fun onFiltersApply(){
        if(state.filtroEstadoAplicado || state.filtroImporteAplicado || state.filtroFechaAplicado){
            if(state.filtroEstadoAplicado)
                state = state.copy(facturas = facturasOriginal.filter {
                        factura ->
                    factura.descEstado == state.estados.keys.firstOrNull{
                            key ->
                        key == factura.descEstado && state.estados.getValue(key) == true
                    }
                }.toMutableList())

            Log.i("INFO ESTADOS SELECCIONADOS",state.estados.filter { it.value == true }.toList().joinToString(","))
            Log.i("INFO FILTRO",state.facturas.joinToString(","))
            FacturaRepository.setFiltersApplied(true)
            FacturaRepository.setIds(state.facturas.map {
                it.id
            }.toMutableList())
            state = state.copy(filtroImporteAplicado = false, filtroFechaAplicado = false, filtroEstadoAplicado = false)
        }
        else
            FacturaRepository.setIds(facturasOriginal.map {
                it.id
            }.toMutableList())
    }
}
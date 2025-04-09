package com.example.core.ui.screens.facturas.list.usecase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FacturaSharedViewModel @Inject constructor() : ViewModel() {
    var idList by mutableStateOf<List<Int>>(emptyList())
        private set

    var filtersApplied by mutableStateOf(false)
        private set

    fun setIds(ids : List<Int>){
        idList = ids
    }

    fun getIds() : List<Int> = idList

    fun setFilters(value : Boolean){
        filtersApplied = value
    }

    fun getFilters() : Boolean = filtersApplied
}
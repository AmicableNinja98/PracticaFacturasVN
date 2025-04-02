package com.example.practicafacturas.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.core.ui.screens.facturas.list.ui.FacturaListFilterScreen
import com.example.core.ui.screens.facturas.list.ui.FacturaListScreenHost
import com.example.core.ui.screens.facturas.list.usecase.FacturaListViewModel

object FacturaNavGraph {
    const val ROUTE = "factura"
    fun list() = "$ROUTE/list"
    fun filter() = "$ROUTE/filter"
}

fun NavGraphBuilder.facturaGraph(navController: NavController){
    navigation(startDestination = FacturaNavGraph.list(),route = FacturaNavGraph.ROUTE) {
        list(navController)
        filter(navController)
    }
}

private fun NavGraphBuilder.list(navController: NavController){
    composable(route = FacturaNavGraph.list()) {
        FacturaListScreenHost(hiltViewModel<FacturaListViewModel>(), goToFilter = {
            navController.navigate(FacturaNavGraph.filter())
        })
    }
}

private fun NavGraphBuilder.filter(navController: NavController){
    composable(route = FacturaNavGraph.filter()) {
        FacturaListFilterScreen(
            goBack = {
                navController.popBackStack()
            }
        )
    }
}
package com.example.practicafacturas.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.core.ui.screens.facturas.list.ui.FacturaListFilterHost
import com.example.core.ui.screens.facturas.list.ui.FacturaListScreenHost
import com.example.core.ui.screens.facturas.list.usecase.filter.FacturaListFilterViewModel
import com.example.core.ui.screens.facturas.list.usecase.list.FacturaListViewModel
import com.example.core.ui.screens.facturas.list.usecase.FacturaSharedViewModel

object FacturaNavGraph {
    const val ROUTE = "factura"
    fun list() = "$ROUTE/list"
    fun filter() = "$ROUTE/filter"
}

fun NavGraphBuilder.facturaGraph(navController: NavController) {
    navigation(startDestination = FacturaNavGraph.list(), route = FacturaNavGraph.ROUTE) {
        list(navController)
        filter(navController)
    }
}

private fun NavGraphBuilder.list(navController: NavController) {
    composable(route = FacturaNavGraph.list()) { backStackEntry ->
        val parentEntry =
            remember(backStackEntry) { navController.getBackStackEntry(FacturaNavGraph.ROUTE) }
        val sharedViewModel = hiltViewModel<FacturaSharedViewModel>(parentEntry)
        FacturaListScreenHost(hiltViewModel<FacturaListViewModel>(), sharedViewModel, goToFilter = {
            navController.navigate(FacturaNavGraph.filter())
        })
    }
}

private fun NavGraphBuilder.filter(navController: NavController) {
    composable(route = FacturaNavGraph.filter()) { backStackEntry ->
        val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(FacturaNavGraph.ROUTE) }
        val sharedViewModel = hiltViewModel<FacturaSharedViewModel>(parentEntry)
        FacturaListFilterHost(
            hiltViewModel<FacturaListFilterViewModel>(),
            sharedViewModel,
            goBack = {
                navController.popBackStack()
            }
        )
    }
}
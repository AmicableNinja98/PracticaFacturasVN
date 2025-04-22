package com.example.practicafacturas.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.core.ui.screens.facturas.ui.FacturaListFilterHost
import com.example.core.ui.screens.facturas.ui.FacturaListScreenHost
import com.example.core.ui.screens.facturas.usecase.filter.FacturaListFilterViewModel
import com.example.core.ui.screens.facturas.usecase.list.FacturaListViewModel
import com.example.core.ui.screens.facturas.usecase.shared.FacturaSharedViewModel

fun NavGraphBuilder.facturaGraph(navController: NavController) {
    navigation(startDestination = AppNavGraph.FACTURA_LIST, route = AppNavGraph.FACTURA) {
        list(navController)
        filter(navController)
    }
}

private fun NavGraphBuilder.list(navController: NavController) {
    composable(route = AppNavGraph.FACTURA_LIST,
        arguments = listOf(
            navArgument("useJson"){
                type = NavType.BoolType
                defaultValue = false
            }
        )) { backStackEntry ->
        val useJson = backStackEntry.arguments?.getBoolean("useJson") ?: false
        val parentEntry =
            remember(backStackEntry) { navController.getBackStackEntry(AppNavGraph.FACTURA) }
        val sharedViewModel = hiltViewModel<FacturaSharedViewModel>(parentEntry)
        FacturaListScreenHost(hiltViewModel<FacturaListViewModel>(), sharedViewModel = sharedViewModel,useMock = useJson, goToFilter = {
            navController.navigate(AppNavGraph.FACTURA_FILTER)
        },goBack = {
            navController.popBackStack()
        })
    }
}

private fun NavGraphBuilder.filter(navController: NavController) {
    composable(route = AppNavGraph.FACTURA_FILTER) { backStackEntry ->
        val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(AppNavGraph.FACTURA) }
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
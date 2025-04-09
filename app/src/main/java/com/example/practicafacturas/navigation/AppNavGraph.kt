package com.example.practicafacturas.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.core.ui.screens.smartSolar.ui.SmartSolarScreen
import com.example.core.ui.screens.smartSolar.usecase.SmartSolarScreenViewModel
import com.example.practicafacturas.home.HomeScreen

object AppNavGraph {
    const val ROOT = "root"
    const val HOME = "home"
    const val FACTURA = "factura"
    const val FACTURA_LIST = "$FACTURA/list"
    const val FACTURA_FILTER = "$FACTURA/filter"
    const val SMART_SOLAR = "smart_solar"
}

fun NavGraphBuilder.appGraph(navController: NavController) {
    navigation(startDestination = AppNavGraph.HOME, route = AppNavGraph.ROOT) {
        home(navController)
        facturaGraph(navController)
        smartSolar(navController)
    }
}

private fun NavGraphBuilder.home(navController: NavController) {
    composable(
        AppNavGraph.HOME
    ) {
        HomeScreen(
            {
                navController.navigate(AppNavGraph.FACTURA)
            },
            {
                navController.navigate(AppNavGraph.SMART_SOLAR)
            })
    }
}

private fun NavGraphBuilder.smartSolar(navController: NavController) {
    composable(AppNavGraph.SMART_SOLAR) {
        SmartSolarScreen(
            hiltViewModel<SmartSolarScreenViewModel>(),
            goBack = {
                navController.popBackStack()
            }
        )
    }
}
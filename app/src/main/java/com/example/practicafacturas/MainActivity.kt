package com.example.practicafacturas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.ui.screens.facturas.list.ui.FacturaListFilterScreen
import com.example.core.ui.screens.facturas.list.ui.FacturaListScreenHost
import com.example.core.ui.screens.facturas.list.usecase.FacturaListViewModel
import com.example.practicafacturas.theme.PracticaFacturasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaFacturasTheme {
                //FacturaListScreenHost(hiltViewModel<FacturaListViewModel>())
                FacturaListFilterScreen({})
            }
        }
    }
}
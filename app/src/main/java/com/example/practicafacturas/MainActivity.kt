package com.example.practicafacturas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.practicafacturas.navigation.AppNavGraph
import com.example.practicafacturas.navigation.appGraph
import com.example.practicafacturas.theme.PracticaFacturasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            PracticaFacturasTheme {
                NavHost(
                    navController = navController,
                    startDestination = AppNavGraph.ROOT
                ){
                    appGraph(navController)
                }
            }
        }
    }
}
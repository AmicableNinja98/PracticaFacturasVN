package com.example.core.ui.screens.facturas.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.core.R
import com.example.core.ui.screens.facturas.list.usecase.FacturaListState
import com.example.core.ui.screens.facturas.list.usecase.FacturaListViewModel
import com.example.domain.factura.Factura
import com.example.ui.base.composables.LoadingScreen
import com.example.ui.base.composables.NoDataScreen

@Composable
fun FacturaListScreenHost(viewModel: FacturaListViewModel,goToFilter : () -> Unit) {
    when (viewModel.state) {
        is FacturaListState.Loading -> LoadingScreen("Cargando facturas...")
        is FacturaListState.Success -> FacturaListScreen((viewModel.state as FacturaListState.Success).facturas,goToFilter)
        is FacturaListState.NoData -> NoDataScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaListScreen(facturas: List<Factura>,goToFilter: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Consumo",
                        color = colorResource(R.color.light_orange)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            tint = colorResource(R.color.light_orange),
                            contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = goToFilter
                    ) {
                        Icon(painter = painterResource(R.drawable.filtericon),contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        FacturaList(facturas, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun FacturaList(facturas: List<Factura>, modifier: Modifier) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Facturas",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
        items(facturas) { factura ->
            FacturaItem(factura)
        }
    }
}

@Composable
fun FacturaItem(factura: Factura) {
    val openDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    openDialog.value = true
                }
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = factura.fecha
            )
            Text(
                text = factura.descEstado,
                color = colorResource(R.color.dark_orange)
            )
        }

        Box(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                "${factura.importeOrdenacion} €",
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
    HorizontalDivider(thickness = 1.dp)
    if(openDialog.value){
        FacturaItemPopUp(
            title = "Información",
            message = "Esta funcionalidad aún no está disponible",
            onDismiss = {
                openDialog.value = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaItemPopUp(title: String, message: String, onDismiss: () -> Unit) {
    AlertDialog(
        title = {
            Text(title)
        },
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        text = {
            Text(message)
        },
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cerrar")
            }
        }
    )

}
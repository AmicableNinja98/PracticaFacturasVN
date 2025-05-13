package com.example.core.ui.screens.facturas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.core.extensions.toFormattedDisplayDateOrNull
import com.example.core.extensions.toMillis
import com.example.core.ui.screens.facturas.usecase.list.FacturaListState
import com.example.core.ui.screens.facturas.usecase.list.FacturaListViewModel
import com.example.core.ui.screens.facturas.usecase.shared.FacturaSharedViewModel
import com.example.domain.factura.Factura
import com.example.ui.base.composables.BaseAlertDialog
import com.example.ui.base.composables.LoadingScreen
import com.example.ui.base.composables.NoDataScreen
import com.example.ui.base.composables.appbar.AppBarActions
import com.example.ui.base.composables.appbar.BaseTopAppBar
import com.example.ui.base.composables.appbar.BaseTopAppBarState
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class FacturaListEvents(
    val goToFilter: () -> Unit,
    val goBack: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaListScreenHost(
    facturaListViewModel: FacturaListViewModel,
    useMock: Boolean,
    sharedViewModel: FacturaSharedViewModel,
    goToFilter: () -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            BaseTopAppBar(
                appBarState = getTopAppBarState(
                    events = FacturaListEvents(
                        goToFilter = goToFilter,
                        goBack = goBack,
                    )
                )
            )
        },
    ) { innerPadding ->
        LaunchedEffect(Unit) {
            facturaListViewModel.getFacturasFromApiOrDatabase(sharedViewModel, useMock)
        }
        when (facturaListViewModel.state) {
            is FacturaListState.Loading -> LoadingScreen(
                stringResource(R.string.loading_screen_title),
                modifier = Modifier.padding(innerPadding)
            )

            is FacturaListState.Success -> FacturaListScreen(
                (facturaListViewModel.state as FacturaListState.Success).facturas,
                modifier = Modifier.padding(innerPadding)
            )

            is FacturaListState.NoData -> NoDataScreen(Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun FacturaListScreen(facturas: List<Factura>, modifier: Modifier) {
    var columnChartSelected by remember { mutableStateOf(false) }
    var dataMap: Map<Double, Number> = mapOf()
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.factura_list_title),
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("€")
                    Switch(
                        checked = columnChartSelected,
                        onCheckedChange = {
                            columnChartSelected = it
                        },
                        colors = SwitchColors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color.LightGray,
                            checkedBorderColor = Color.Black,
                            checkedIconColor = Color.LightGray,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = colorResource(R.color.green_button),
                            uncheckedBorderColor = Color.Black,
                            uncheckedIconColor = Color.Black,
                            disabledCheckedThumbColor = Color.Black,
                            disabledCheckedTrackColor = Color.Black,
                            disabledCheckedBorderColor = Color.Black,
                            disabledCheckedIconColor = Color.Black,
                            disabledUncheckedThumbColor = Color.Black,
                            disabledUncheckedTrackColor = Color.Black,
                            disabledUncheckedBorderColor = Color.Black,
                            disabledUncheckedIconColor = Color.Black
                        )
                    )
                    Text("khW")
                }

                when (columnChartSelected) {
                    true -> {
                        dataMap = facturas.associate {
                            ((it.fecha.toMillis() ?: 0).toDouble()) to it.energiaConsumida
                        }
                        FacturaChart(dataMap, columnChartSelected)
                    }

                    false -> {
                        dataMap = facturas.associate {
                            ((it.fecha.toMillis() ?: 0).toDouble()) to it.importeOrdenacion
                        }
                        FacturaChart(dataMap, columnChartSelected)
                    }
                }

            }
        }
        items(facturas) { factura ->
            FacturaItem(factura)
        }
    }
}


@Composable
fun FacturaChart(dataMap: Map<Double, Number>, columnChartSelected: Boolean) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            if (columnChartSelected)
                columnSeries {
                    series(x = dataMap.keys, y = dataMap.values)
                }
            else
                lineSeries {
                    series(x = dataMap.keys, y = dataMap.values)
                }
        }
    }

    val bottomAxisValueFormatter = object : CartesianValueFormatter {
        override fun format(
            context: CartesianMeasuringContext,
            value: Double,
            verticalAxisPosition: Axis.Position.Vertical?
        ): CharSequence {
            val date = Date(value.toLong())
            return SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(date)
        }
    }

    CartesianChartHost(
        chart =
            rememberCartesianChart(
                if (columnChartSelected) rememberColumnCartesianLayer()
                else rememberLineCartesianLayer(
                    lineProvider = LineCartesianLayer.LineProvider.series(
                        vicoTheme.lineCartesianLayerColors.map { color ->
                            LineCartesianLayer.rememberLine(
                                LineCartesianLayer.LineFill.single(
                                    fill(colorResource(R.color.green_button))
                                ),
                                pointConnector = LineCartesianLayer.PointConnector.cubic(1f),
                            )
                        }
                    ),
                ),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = bottomAxisValueFormatter,
                    itemPlacer = HorizontalAxis.ItemPlacer.aligned(spacing = {30})
                ),
            ),
        modelProducer = modelProducer
    )
}

@Composable
fun FacturaItem(factura: Factura) {
    val openDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
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
                text = factura.fecha.toFormattedDisplayDateOrNull() ?: "",
                fontSize = 20.sp
            )
            Text(
                text = getFacturaStateText(factura),
                fontSize = 17.sp,
                color = getFacturaStateColor(factura)
            )
        }

        Box(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Row()
            {
                Text(
                    "${"%.2f".format(factura.importeOrdenacion)} €",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }
    HorizontalDivider(thickness = 1.dp)
    if (openDialog.value) {
        FacturaItemPopUp(
            title = stringResource(R.string.popUp_title),
            message = stringResource(R.string.popUp_message),
            onDismiss = {
                openDialog.value = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaItemPopUp(title: String, message: String, onDismiss: () -> Unit) {
    BaseAlertDialog(
        title = title,
        message = message,
        onDismiss = onDismiss,
        closeButtonText = stringResource(R.string.close_popUp)
    )
}

@Composable
private fun getFacturaStateText(factura: Factura): String {
    return when (factura.descEstado) {
        "Pagada" -> stringResource(R.string.factura_descState_pagada)
        "Anulada" -> stringResource(R.string.factura_descState_anulada)
        "Cuota fija" -> stringResource(R.string.factura_descState_cuota_fija)
        "Pendiente de pago" -> stringResource(R.string.factura_descState_pendiente_de_pago)
        "Plan de pago" -> stringResource(R.string.factura_descState_plan_de_pago)
        else -> ""
    }
}

@Composable
private fun getFacturaStateColor(factura: Factura): Color {
    return when (factura.descEstado) {
        "Pagada" -> colorResource(R.color.green_button)
        "Anulada" -> Color.Red
        "Cuota fija" -> colorResource(R.color.green_button)
        "Pendiente de pago" -> Color.Red
        "Plan de pago" -> colorResource(R.color.green_button)
        else -> Color.White
    }
}

@Composable
fun getTopAppBarState(
    events: FacturaListEvents,
): BaseTopAppBarState {
    return BaseTopAppBarState(
        title = stringResource(R.string.facturaList_screen_appbar_title),
        icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
        goBackAction = events.goBack,
        actions = listOf(
            AppBarActions.PainterAppBarActions(
                icon = painterResource(R.drawable.filtericon),
                contentDescription = "",
                onClick = {
                    events.goToFilter()
                }
            )
        )
    )
}
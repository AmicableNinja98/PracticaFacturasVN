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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.core.extensions.getFacturaStateName
import com.example.core.extensions.toFormattedDisplayDateOrNull
import com.example.core.ui.screens.facturas.usecase.list.FacturaListState
import com.example.core.ui.screens.facturas.usecase.list.FacturaListViewModel
import com.example.core.ui.screens.facturas.usecase.shared.FacturaSharedViewModel
import com.example.domain.appstrings.AppStrings
import com.example.domain.factura.Factura
import com.example.ui.base.composables.BaseAlertDialog
import com.example.ui.base.composables.LoadingScreen
import com.example.ui.base.composables.NoDataScreen
import com.example.ui.base.composables.appbar.AppBarActions
import com.example.ui.base.composables.appbar.BaseTopAppBar
import com.example.ui.base.composables.appbar.BaseTopAppBarState
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line

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
    val strings = facturaListViewModel.strings.collectAsState()

    Scaffold(
        topBar = {
            BaseTopAppBar(
                appBarState = getTopAppBarState(
                    strings = strings,
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
                strings.value?.loadingScreenTitle ?: stringResource(R.string.loading_screen_title),
                modifier = Modifier.padding(innerPadding)
            )

            is FacturaListState.Success -> FacturaListScreen(
                facturas = (facturaListViewModel.state as FacturaListState.Success).facturas,
                showChart = facturaListViewModel.showGraph,
                strings = strings,
                modifier = Modifier.padding(innerPadding)
            )

            is FacturaListState.NoData -> NoDataScreen(text = strings.value?.noDataScreenText,Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun FacturaListScreen(facturas: List<Factura>, showChart : Boolean, strings: State<AppStrings?>, modifier: Modifier) {
    var columnChartSelected by remember { mutableStateOf(false) }
    var dataMap: Map<String, Number> = mapOf()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = strings.value?.facturaListTitle ?: stringResource(R.string.factura_list_title),
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        if(showChart){
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
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
                }
            }

            item {
                LazyRow {
                    item {
                        dataMap = when (columnChartSelected) {
                            true -> {
                                facturas.associate {
                                    (it.fecha.toFormattedDisplayDateOrNull() ?: "") to it.energiaConsumida
                                }

                            }

                            false -> {
                                facturas.associate {
                                    (it.fecha.toFormattedDisplayDateOrNull() ?: "") to it.importeOrdenacion
                                }
                            }
                        }
                        FacturaChartHost(dataMap, columnChartSelected,strings)
                    }
                }
            }
        }
        items(facturas) { factura ->
            FacturaItem(factura,strings)
        }
    }
}


@Composable
fun FacturaChartHost(dataMap: Map<String, Number>, columnChartSelected: Boolean,strings: State<AppStrings?>) {
    val indicatorProperties = HorizontalIndicatorProperties(
        enabled = true,
        padding = 10.dp,
        textStyle = MaterialTheme.typography.labelSmall)

        val labelProperties = LabelProperties(
            enabled = true,
            labels = dataMap.keys.toList(),
        )

    if(columnChartSelected){
        FacturaColumnChart(dataMap,indicatorProperties,labelProperties)
    }
    else{
        FacturaLineChart(dataMap,indicatorProperties,labelProperties,
            eurosLabel = strings.value?.eurosChartLabel ?: stringResource(R.string.euros_Chart_Label)
        )
    }
}

@Composable
fun FacturaLineChart(dataMap: Map<String, Number>,indicatorProperties: HorizontalIndicatorProperties,labelProperties: LabelProperties,eurosLabel : String){
    LineChart(
        modifier = Modifier
            .widthIn(min = 600.dp, max = 900.dp)
            .heightIn(min = 400.dp, max = 600.dp)
            .padding(vertical = 10.dp),
        data = remember {
            listOf(Line(
                label = eurosLabel,
                values = dataMap.values.map { it.toDouble() },
                color = SolidColor(Color.Green)
            ))
        },
        indicatorProperties = indicatorProperties,  
        labelProperties = labelProperties,
        dotsProperties = DotProperties(
            enabled = true,
            color = SolidColor(Color.Green),
            radius = 4.dp
        )
    )
}

@Composable
fun FacturaColumnChart(dataMap: Map<String, Number>,indicatorProperties: HorizontalIndicatorProperties,labelProperties: LabelProperties){
    ColumnChart(
        modifier = Modifier
            .widthIn(min = 600.dp, max = 900.dp)
            .heightIn(min = 400.dp, max = 600.dp)
            .padding(vertical = 10.dp),
        data = remember {
            dataMap.keys.map {
                Bars(
                    label = it,
                    values = listOf(
                        Bars.Data(
                            label = "KwH",
                            value = (dataMap[it] ?: 0).toDouble(),
                            color = SolidColor(Color.Green)
                        )
                    )
                )
            }
        },
        indicatorProperties = indicatorProperties,
        labelProperties = labelProperties,
        barProperties = BarProperties(
            cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
            spacing = 3.dp,
            thickness = 20.dp
        ),
    )
}

@Composable
fun FacturaItem(factura: Factura,strings: State<AppStrings?>) {
    val openDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable(
                onClick = {
                    openDialog.value = true
                }),
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
                text = getFacturaStateName(factura.descEstado,strings),
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
            title = strings.value?.popUpTitle ?: stringResource(R.string.popUp_title),
            message = strings.value?.popUpMessage ?: stringResource(R.string.popUp_message),
            closeText = strings.value?.closePopUp ?: stringResource(R.string.close_popUp),
            onDismiss = {
                openDialog.value = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaItemPopUp(title: String, message: String,closeText : String ,onDismiss: () -> Unit) {
    BaseAlertDialog(
        title = title,
        message = message,
        onDismiss = onDismiss,
        closeButtonText = closeText
    )
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
    strings: State<AppStrings?>,
    events: FacturaListEvents,
): BaseTopAppBarState {
    return BaseTopAppBarState(
        title = strings.value?.facturaListScreenAppbarTitle ?: stringResource(R.string.facturaList_screen_appbar_title),
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
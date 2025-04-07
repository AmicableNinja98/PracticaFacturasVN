package com.example.core.ui.screens.facturas.list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.core.ui.screens.facturas.list.usecase.FacturaListFilterViewModel

@Composable
fun FacturaListFilterHost(
    facturaListFilterViewModel: FacturaListFilterViewModel,
    goBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        facturaListFilterViewModel.getFacturasFromRepo()
    }

    if (facturaListFilterViewModel.state.sinDatos) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay facturas para filtrar")
        }
    } else {
        FacturaListFilterScreen(facturaListFilterViewModel, goBack)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaListFilterScreen(
    facturaListFilterViewModel: FacturaListFilterViewModel,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(
                        onClick = goBack
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close_icon),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        FacturaListFilter(facturaListFilterViewModel,goBack ,Modifier.padding(innerPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaListFilter(facturaListFilterViewModel: FacturaListFilterViewModel,goBack: () -> Unit ,modifier: Modifier) {
    var sliderValue by remember {
        mutableStateOf(
            0f..facturaListFilterViewModel.state.importeMax.toFloat()
        )
    }
    val openDialogFirstDate = remember { mutableStateOf(false) }
    val openDialogSecondDate = remember { mutableStateOf(false) }
    val datePickerFirstDateState = rememberDatePickerState()
    val datePickerSecondDateState = rememberDatePickerState()

    Column(
        modifier = modifier
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Filtrar Facturas",
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Text(
            text = "Con fecha de emisión",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        Row(
            modifier = Modifier.padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "Desde:")
                Button(
                    onClick = {
                        openDialogFirstDate.value = true
                    },
                    colors = ButtonColors(
                        containerColor = colorResource(R.color.light_gray),
                        contentColor = Color.Black,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.LightGray
                    )
                ) {
                    Text(facturaListFilterViewModel.state.fechaInicio ?: "día/mes/año")
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "Hasta:")
                Button(
                    onClick = {
                        openDialogSecondDate.value = true
                    },
                    colors = ButtonColors(
                        containerColor = colorResource(R.color.light_gray),
                        contentColor = Color.Black,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.LightGray
                    )
                ) {
                    Text(facturaListFilterViewModel.state.fechaFin ?: "día/mes/año")
                }
            }
        }
        if(openDialogFirstDate.value){
            FacturaDatePicker(
                onDismissRequest = {
                    openDialogFirstDate.value = false
                },
                onClick = {
                    facturaListFilterViewModel.onStartDateChanged(datePickerFirstDateState.selectedDateMillis)
                    openDialogFirstDate.value = false
                },
                datePickerState = datePickerFirstDateState
            )
        }
        if(openDialogSecondDate.value){
            FacturaDatePicker(
                onDismissRequest = {
                    openDialogSecondDate.value = false
                },
                onClick = {
                    facturaListFilterViewModel.onEndDateChanged(datePickerSecondDateState.selectedDateMillis)
                    openDialogSecondDate.value = false
                },
                datePickerState = datePickerSecondDateState
            )
        }
        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 15.dp))
        Text(
            text = "Por un importe",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${facturaListFilterViewModel.state.importeMin} € - ${facturaListFilterViewModel.state.importeMax} €",
                color = colorResource(R.color.light_orange)
            )
        }
        RangeSlider(
            value = sliderValue,
            modifier = Modifier.padding(10.dp),
            onValueChange = {
                sliderValue = it
                facturaListFilterViewModel.onSliderValueChange(sliderValue)
            },
            valueRange = 0f..sliderValue.endInclusive,
            colors = SliderColors(
                thumbColor = colorResource(R.color.dark_orange),
                activeTrackColor = colorResource(R.color.dark_orange),
                activeTickColor = colorResource(R.color.dark_orange),
                inactiveTrackColor = Color.LightGray,
                inactiveTickColor = Color.LightGray,
                disabledThumbColor = Color.LightGray,
                disabledActiveTrackColor = Color.LightGray,
                disabledActiveTickColor = Color.LightGray,
                disabledInactiveTrackColor = Color.LightGray,
                disabledInactiveTickColor = Color.LightGray,
            )
        )
        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 15.dp))
        Text(
            text = "Por estado",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        facturaListFilterViewModel.state.estados.keys.forEach {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = facturaListFilterViewModel.state.estados.getValue(it),
                    onCheckedChange = { value ->
                        facturaListFilterViewModel.onCheckedChange(it)
                    }
                )
                Text(it)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        facturaListFilterViewModel.onApplyFiltersClick()
                        goBack()
                    },
                    colors = ButtonColors(
                        containerColor = colorResource(R.color.green_button),
                        contentColor = Color.White,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.LightGray
                    ),
                    modifier = Modifier.width(200.dp)
                ) {
                    Text("Aplicar")
                }
                Button(
                    onClick = {
                        facturaListFilterViewModel.onFiltersReset()
                    },
                    colors = ButtonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.LightGray
                    ),
                    modifier = Modifier.width(200.dp)
                ) {
                    Text("Eliminar filtros")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaDatePicker(onDismissRequest: () -> Unit,onClick : () -> Unit,datePickerState: DatePickerState){
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = onClick
            ) {
                Text("Confirmar")
            }
        }
    ){
        DatePicker(
            state = datePickerState
        )
    }
}
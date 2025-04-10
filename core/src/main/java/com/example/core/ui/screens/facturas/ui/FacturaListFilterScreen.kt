package com.example.core.ui.screens.facturas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.core.ui.screens.facturas.usecase.FacturaSharedViewModel
import com.example.core.ui.screens.facturas.usecase.filter.FacturaListFilterViewModel
import com.example.ui.base.composables.BaseAlertDialog
import com.example.ui.base.composables.BaseButton

@Composable
fun FacturaListFilterHost(
    facturaListFilterViewModel: FacturaListFilterViewModel,
    facturaSharedViewModel: FacturaSharedViewModel,
    goBack: () -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        facturaListFilterViewModel.getFacturasFromRepository()
    }

    if (facturaListFilterViewModel.state.sinDatos) {
        openDialog.value = true
        if (openDialog.value) {
            NoDataFilterPopUp(
                title = stringResource(R.string.filter_popUp_title),
                message = stringResource(R.string.filter_popUp_message),
                onDismiss = {
                    openDialog.value = false
                    facturaListFilterViewModel.onFiltersReset(facturaSharedViewModel)
                }
            )
        }
    } else {
        FacturaListFilterScreen(facturaListFilterViewModel, facturaSharedViewModel, goBack)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaListFilterScreen(
    facturaListFilterViewModel: FacturaListFilterViewModel,
    facturaSharedViewModel: FacturaSharedViewModel,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.filter_screen_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                },
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
        FacturaListFilter(
            facturaListFilterViewModel,
            facturaSharedViewModel,
            goBack,
            Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaListFilter(
    facturaListFilterViewModel: FacturaListFilterViewModel,
    facturaSharedViewModel: FacturaSharedViewModel,
    goBack: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .padding(20.dp)
    ) {
        SeccionFechas(facturaListFilterViewModel)
        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 15.dp))
        SliderSection(facturaListFilterViewModel)
        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 15.dp))
        SeccionCheckBox(facturaListFilterViewModel)
        SeccionBotones(facturaListFilterViewModel, facturaSharedViewModel, goBack)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeccionFechas(facturaListFilterViewModel: FacturaListFilterViewModel) {
    val openDialogFirstDate = remember { mutableStateOf(false) }
    val openDialogSecondDate = remember { mutableStateOf(false) }
    val datePickerFirstDateState = rememberDatePickerState()
    val datePickerSecondDateState = rememberDatePickerState()

    Text(
        text = stringResource(R.string.filter_date_title),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    )
    Row(
        modifier = Modifier.padding(top = 15.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DatePickerButton(title = stringResource(R.string.filter_startDate_title), buttonText = facturaListFilterViewModel.state.fechaInicio
            ?: stringResource(R.string.filter_date_button_text), onClick = {
            openDialogFirstDate.value = true
        })
        DatePickerButton(title = stringResource(R.string.filter_endDate_title), buttonText = facturaListFilterViewModel.state.fechaFin
            ?: stringResource(R.string.filter_date_button_text), onClick = {
            openDialogSecondDate.value = true
        })
    }
    if (openDialogFirstDate.value) {
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
    if (openDialogSecondDate.value) {
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
}

@Composable
fun DatePickerButton(title: String,buttonText : String,onClick: () -> Unit){
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = title)
        BaseButton(
            text = buttonText,
            onClick = onClick,
            colors = ButtonColors(
                containerColor = colorResource(R.color.light_gray),
                contentColor = Color.Black,
                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.LightGray
            )
        )
    }
}

@Composable
fun SliderSection(facturaListFilterViewModel: FacturaListFilterViewModel) {
    Text(
        text = stringResource(R.string.filter_importe_title),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${"%.2f".format(facturaListFilterViewModel.state.importeMin)} € - ${
                "%.2f".format(
                    facturaListFilterViewModel.state.importeMax
                )
            } €",
            color = colorResource(R.color.light_orange)
        )
    }
    ImporteSlider(
        facturaListFilterViewModel = facturaListFilterViewModel
    )
}

@Composable
fun SeccionCheckBox(facturaListFilterViewModel: FacturaListFilterViewModel) {
    Box(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)
    ) {
        Text(
            text = stringResource(R.string.filter_checkBox_title),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
    }
    facturaListFilterViewModel.state.estados.keys.forEach {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
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
}

@Composable
fun SeccionBotones(
    facturaListFilterViewModel: FacturaListFilterViewModel,
    sharedViewModel: FacturaSharedViewModel,
    goBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BaseButton(
                text = stringResource(R.string.filter_applyButton_text),
                colors = ButtonColors(
                    containerColor = colorResource(R.color.green_button),
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.LightGray
                ),
                onClick = {
                    facturaListFilterViewModel.onApplyFiltersClick(sharedViewModel)
                    if (!facturaListFilterViewModel.state.sinDatos) {
                        goBack()
                    }
                },
                modifier = Modifier.width(200.dp)
            )
            BaseButton(
                text = stringResource(R.string.filter_resetButton_text),
                onClick = {
                    facturaListFilterViewModel.onFiltersReset(sharedViewModel)
                },
                colors = ButtonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.LightGray
                ),
                modifier = Modifier.width(200.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaDatePicker(
    onDismissRequest: () -> Unit,
    onClick: () -> Unit,
    datePickerState: DatePickerState
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = onClick
            ) {
                Text(stringResource(R.string.filter_popUp_confirm_text))
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@Composable
fun ImporteSlider(
    facturaListFilterViewModel: FacturaListFilterViewModel,
    modifier: Modifier = Modifier,
    steps: Int = 5
) {
    val importeMinAbsoluto = facturaListFilterViewModel.state.facturas.minOfOrNull { it.importeOrdenacion }
            ?.toFloat() ?: 0f
    val importeMaxAbsoluto = facturaListFilterViewModel.state.facturas.maxOfOrNull { it.importeOrdenacion }
            ?.toFloat() ?: 0f
    val sliderRange = importeMinAbsoluto..importeMaxAbsoluto

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${"%.2f".format(importeMinAbsoluto)} €",
                color = Color.LightGray
            )
            Text(
                text = "${"%.2f".format(importeMaxAbsoluto)} €",
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        RangeSlider(
            value = facturaListFilterViewModel.state.importeMin.toFloat()..facturaListFilterViewModel.state.importeMax.toFloat(),
            onValueChange = { range ->
                facturaListFilterViewModel.onSliderValueChange(range)
            },
            valueRange = sliderRange,
            steps = steps,
            colors = SliderColors(
                thumbColor = colorResource(id = R.color.dark_orange),
                activeTrackColor = colorResource(id = R.color.dark_orange),
                activeTickColor = colorResource(id = R.color.dark_orange),
                inactiveTrackColor = Color.LightGray,
                inactiveTickColor = Color.LightGray,
                disabledThumbColor = Color.LightGray,
                disabledActiveTrackColor = Color.LightGray,
                disabledActiveTickColor = Color.LightGray,
                disabledInactiveTrackColor = Color.LightGray,
                disabledInactiveTickColor = Color.LightGray,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoDataFilterPopUp(title: String, message: String, onDismiss: () -> Unit) {
    BaseAlertDialog(
        title = title,
        message = message,
        onDismiss = onDismiss,
        closeButtonText = stringResource(R.string.filter_popUp_close_text)
    )
}
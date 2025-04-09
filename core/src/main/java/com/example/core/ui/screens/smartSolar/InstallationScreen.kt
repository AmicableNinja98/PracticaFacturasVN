package com.example.core.ui.screens.smartSolar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.ui.base.composables.BaseReadOnlyTextField

@Composable
fun InstallationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.smartSolar_installation_title),
            modifier = Modifier.padding(bottom = 28.dp)
        )
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = colorResource(R.color.gray_text))) {
                append("Autoconsumo: ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("92%") }
        })
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Image(
                painter = painterResource(R.drawable.grafico1),
                modifier = Modifier.size(400.dp),
                contentDescription = null
            )
        }
    }
}

@Composable
fun EnergyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.plan_gestiones),
            modifier = Modifier.size(200.dp),
            contentDescription = null
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 20.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = stringResource(R.string.smartSolar_energy_screen_text)
            )
        }
    }
}

@Composable
fun DetailsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BaseReadOnlyTextField(
            title = stringResource(R.string.smartSolar_details_code_field_title),
            input = ""
        )
        BaseReadOnlyTextField(
            title = stringResource(R.string.smartSolar_details_request_status_title),
            input = ""
        )
        BaseReadOnlyTextField(
            title = stringResource(R.string.smartSolar_details_type_title),
            input = ""
        )
        BaseReadOnlyTextField(
            title = stringResource(R.string.smartSolar_details_compensation_field_title),
            input = ""
        )
        BaseReadOnlyTextField(
            title = stringResource(R.string.smartSolar_details_power_field_title),
            input = ""
        )
    }
}
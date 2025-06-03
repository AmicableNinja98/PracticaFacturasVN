package com.example.practicafacturas.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practicafacturas.R
import com.example.practicafacturas.home.usecase.HomeScreenViewModel

@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel,
    onNavigateToFacturas: (Boolean) -> Unit,
    onNavigateToSmartSolar: () -> Unit
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(homeScreenViewModel.snackbarHostState)
        }
    ) { innerPadding ->
        HomeScreenBody(
            homeScreenViewModel,
            onNavigateToFacturas,
            onNavigateToSmartSolar,
            Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun HomeScreenBody(
    homeScreenViewModel: HomeScreenViewModel,
    onNavigateToFacturas: (Boolean) -> Unit,
    onNavigateToSmartSolar: () -> Unit,
    modifier: Modifier
) {
    val snackBarMessages : List<String> = listOf(stringResource(R.string.snackbar_usingMock_message),
        stringResource(R.string.snackbar_usingApi_message)
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.homeScreen_title),
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
        )
        Image(painter = painterResource(R.drawable.logoiberdrola),contentDescription = null)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                HomeCard(
                    title = stringResource(R.string.homeScreen_facturasCard_title),
                    icon = Icons.AutoMirrored.Filled.List,
                    onClick = {
                        onNavigateToFacturas(homeScreenViewModel.useMockData)
                    }
                )
            }
            item {
                HomeCard(
                    title = stringResource(R.string.homeScreen_smartSolarCard_title),
                    icon = Icons.Filled.Star,
                    onClick = onNavigateToSmartSolar
                )
            }
        }
        Text(text = if (homeScreenViewModel.useMockData) stringResource(R.string.switch_useMockData_affirmativeText) else stringResource(
            R.string.switch_useMockData_negativeText
        ))
        Switch(
            checked = homeScreenViewModel.useMockData,
            onCheckedChange = { value ->
                homeScreenViewModel.onSwitchCheckedChange(value)
                homeScreenViewModel.showSnackbarMessage(snackBarMessages)
            },
            colors = SwitchColors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colorResource(R.color.light_orange),
                checkedBorderColor = Color.Black,
                checkedIconColor = colorResource(R.color.light_orange),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
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
        Button(
            onClick = {
                throw RuntimeException("Crash de prueba")
            }
        ) {
            Text("Kaboom")
        }
    }
}

@Composable
fun HomeCard(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(60.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}


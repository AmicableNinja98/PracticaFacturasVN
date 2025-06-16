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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.collectAsState
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
import com.example.ui.base.composables.BaseButton

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
    val strings = homeScreenViewModel.strings.collectAsState()

    val snackBarMessages: List<String> = listOf(
        strings.value?.snackbarUsingMockMessage
            ?: stringResource(R.string.snackbar_usingMock_message),
        strings.value?.snackbarUsingApiMessage ?: stringResource(R.string.snackbar_usingApi_message)
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
            text = strings.value?.homeScreenTitle ?: stringResource(R.string.homeScreen_title),
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
        )
        Image(painter = painterResource(R.drawable.logoiberdrola), contentDescription = null)
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
                    title = strings.value?.homeScreenFacturasCardTitle
                        ?: stringResource(R.string.homeScreen_facturasCard_title),
                    icon = Icons.AutoMirrored.Filled.List,
                    onClick = {
                        onNavigateToFacturas(homeScreenViewModel.useMockData)
                    }
                )
            }
            item {
                HomeCard(
                    title = if(!strings.value?.homeScreenSmartSolarCardTitle.isNullOrEmpty()) strings.value?.homeScreenSmartSolarCardTitle ?: "" else
                        stringResource(R.string.homeScreen_smartSolarCard_title),
                    icon = Icons.Filled.Star,
                    onClick = onNavigateToSmartSolar
                )
            }
        }
        Text(
            text = if (homeScreenViewModel.useMockData) strings.value?.switchUseMockDataAffirmativeText
                ?: stringResource(R.string.switch_useMockData_affirmativeText) else strings.value?.switchUseMockDataNegativeText
                ?: stringResource(R.string.switch_useMockData_negativeText)
        )
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
        BaseButton(
            text = strings.value?.crashButtonText ?: stringResource(R.string.crash_button_text),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.green_button)
            ),
            onClick = {
                throw RuntimeException("Crash de prueba")
            }
        )
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


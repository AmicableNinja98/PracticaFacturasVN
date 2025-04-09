package com.example.core.ui.screens.smartSolar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartSolarScreen(goBack : () -> Unit) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabData = listOf("Mi instalación", "Energía", "Detalles")
    val pagerState = rememberPagerState { tabData.size }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Atrás")
                },
                navigationIcon = {
                    IconButton(
                        onClick = goBack
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.White,
                    navigationIconContentColor = colorResource(R.color.light_orange),
                    titleContentColor = colorResource(R.color.light_orange),
                    actionIconContentColor = Color.White,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Text(
                text = "Smart Solar",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                modifier = Modifier.padding(20.dp)
            )
            TabRow(
                selectedTabIndex = tabIndex,
                containerColor = Color.Transparent,
                modifier = Modifier.padding(16.dp),
                divider = {}
            ) {
                tabData.forEachIndexed { index, _ ->
                    Tab(
                        selected = index == tabIndex,
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.Black,
                        onClick = {
                            tabIndex = index
                        },
                    ) {
                        Text(tabData[index])
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) {
                when (tabIndex) {
                    0 -> InstallationScreen()
                    1 -> EnergyScreen()
                    2 -> DetailsScreen()
                }
            }
        }
    }
}

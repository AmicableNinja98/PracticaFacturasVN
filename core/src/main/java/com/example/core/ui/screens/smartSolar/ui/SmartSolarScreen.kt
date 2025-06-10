package com.example.core.ui.screens.smartSolar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.core.ui.screens.smartSolar.usecase.SmartSolarScreenViewModel
import com.example.domain.appstrings.AppStrings
import com.example.ui.base.composables.appbar.BaseTopAppBar
import com.example.ui.base.composables.appbar.BaseTopAppBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartSolarScreen(smartSolarScreenViewModel: SmartSolarScreenViewModel,goBack : () -> Unit) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val strings = smartSolarScreenViewModel.strings.collectAsState()
    val tabData = listOf(
        strings.value?.smartSolarInstallationTabTitle ?: stringResource(R.string.smartSolar_installation_tab_title),
        strings.value?.smartSolarEnergyTabTitle ?: stringResource(R.string.smartSolar_energy_tab_title),
        strings.value?.smartSolarDetailsTabTitle ?: stringResource(R.string.smartSolar_details_tab_title)
    )
    val pagerState = rememberPagerState { tabData.size }

    Scaffold(
        topBar = {
            BaseTopAppBar(getBaseTopAppBarState(goBack, strings = strings))
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Text(
                text = strings.value?.smartSolarScreenTitle ?: stringResource(R.string.smartSolar_screen_title),
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
                userScrollEnabled = false,
                modifier = Modifier.weight(1f)
            ) {
                when (tabIndex) {
                    0 -> InstallationScreen(strings)
                    1 -> EnergyScreen(strings)
                    2 -> DetailsScreenHost(smartSolarScreenViewModel)
                }
            }
        }
    }
}

@Composable
fun getBaseTopAppBarState(goBack: () -> Unit,strings : State<AppStrings?>) : BaseTopAppBarState{
    return BaseTopAppBarState(
        title = strings.value?.smartSolarAppbarTitle ?: stringResource(R.string.smartSolar_appbar_title),
        icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
        goBackAction = goBack,
        actions = listOf()
    )
}
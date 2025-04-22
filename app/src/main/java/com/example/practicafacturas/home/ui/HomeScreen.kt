package com.example.practicafacturas.home.ui

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.practicafacturas.home.usecase.HomeScreenViewModel

@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel,
    onNavigateToFacturas: (Boolean) -> Unit,
    onNavigateToSmartSolar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 12.dp,bottom = 24.dp)
        )
        Text(text = "Usar datos Mock: ${if(homeScreenViewModel.useMockData) "Si" else "No"}")
        Switch(
            checked = homeScreenViewModel.useMockData,
            //enabled = homeScreenViewModel.isSwitchEnabled,
            onCheckedChange = {
                    value ->
                homeScreenViewModel.setUseMockDataValue(value)
            },
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                HomeCard(
                    title = "Facturas",
                    icon = Icons.AutoMirrored.Filled.List,
                    onClick = {
                        onNavigateToFacturas(homeScreenViewModel.useMockData)
                        homeScreenViewModel.setIsSwitchEnabledValue(false)
                    }
                )
            }
            item {
                HomeCard(
                    title = "Smart Solar",
                    icon = Icons.Filled.Star,
                    onClick = onNavigateToSmartSolar
                )
            }
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}


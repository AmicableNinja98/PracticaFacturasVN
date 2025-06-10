package com.example.ui.base.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.ui.R

@Composable
fun NoDataScreen(text : String?,modifier: Modifier = Modifier){
    Box(
        modifier = modifier.fillMaxSize().background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text ?: stringResource(R.string.no_data_screen_text))
    }
}
package com.example.ui.base.composables

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BaseButton(
    text : String,
    colors: ButtonColors,
    onClick : () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onClick,
        colors = colors,
        modifier = modifier
    ) {
        Text(text)
    }
}
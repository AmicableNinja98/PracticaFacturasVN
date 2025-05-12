package com.example.ui.base.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ui.R

@Composable
fun BaseReadOnlyTextField(
    title: String,
    input: String,
    iconRequired: Boolean = false,
    onIconClick: () -> Unit = {}
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        value = input,
        onValueChange = {},
        readOnly = true,
        label = {
            Text(title)
        },
        trailingIcon = {
            if (iconRequired) {
                IconButton(
                    onClick = onIconClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_info_outline_24),
                        tint = Color.Blue,
                        contentDescription = null
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
        )
    )
}
package com.example.ui.base.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.window.DialogProperties
import com.example.ui.R

@Composable
fun BaseAlertDialog(
    title : String,
    message : String,
    closeButtonText : String,
    onDismiss : () -> Unit
){
    AlertDialog(
        title = {
            Text(title)
        },
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        text = {
            Text(message)
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                BaseButton(
                    onClick = onDismiss,
                    colors = ButtonColors(
                        containerColor = colorResource(R.color.green_button),
                        contentColor = Color.White,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent,
                    ),
                    text = closeButtonText
                )
            }
        }
    )
}
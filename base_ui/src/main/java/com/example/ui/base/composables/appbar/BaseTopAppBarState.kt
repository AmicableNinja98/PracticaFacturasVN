package com.example.ui.base.composables.appbar

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class BaseTopAppBarState(
    val title : String,
    val icon : ImageVector,
    val goBackAction: () -> Unit,
    val actions: List<AppBarActions>
)

sealed class AppBarActions(){
    data class ImageVectorAppBarActions(
        val icon : ImageVector,
        val contentDescription : String,
        val onClick : () -> Unit
    ) : AppBarActions()

    data class PainterAppBarActions(
    val icon : Painter,
    val contentDescription : String,
    val onClick : () -> Unit
    ) : AppBarActions()
}
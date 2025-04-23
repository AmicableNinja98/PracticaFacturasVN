package com.example.ui.base.composables.appbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(appBarState: BaseTopAppBarState) {
    TopAppBar(
        title = {
            Text(
                text = appBarState.title,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = appBarState.goBackAction
            ) {
                Icon(
                    imageVector = appBarState.icon,
                    contentDescription = null
                )
            }

        },
        actions = {
            appBarState.actions.forEach { action ->
                when (action) {
                    is AppBarActions.ImageVectorAppBarActions -> {
                        IconButton(
                            onClick = action.onClick
                        ) {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = action.contentDescription
                            )
                        }
                    }

                    is AppBarActions.PainterAppBarActions -> {
                        IconButton(
                            onClick = action.onClick
                        ) {
                            Icon(
                                painter = action.icon,
                                contentDescription = action.contentDescription
                            )
                        }
                    }
                }
            }
        },
        colors = TopAppBarColors(
            containerColor = Color.White,
            scrolledContainerColor = Color.White,
            navigationIconContentColor = colorResource(R.color.light_orange),
            titleContentColor = colorResource(R.color.light_orange),
            actionIconContentColor = Color.Black,
        )
    )
}
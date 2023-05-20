package com.reminmax.pointsapp.ui.screens.chart.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.reminmax.pointsapp.R

@Composable
fun TopApplicationBar(
    title: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 0.dp,
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    //tint = MaterialTheme.colors.primaryColorAlpha40,
                    contentDescription = stringResource(R.string.goBack)
                )
            }
        },
    )
}
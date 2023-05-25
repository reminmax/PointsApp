package com.reminmax.pointsapp.ui.screens.chart.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reminmax.pointsapp.R
import com.reminmax.pointsapp.ui.theme.PointsAppTheme

@Composable
fun TopApplicationBar(
    title: String,
    onNavigateBack: () -> Unit,
    onSaveChartToFile: () -> Unit,
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
        actions = {
            IconButton(onClick = { onSaveChartToFile() }) {
                Icon(
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = stringResource(R.string.saveToFile),
                    //tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun TopApplicationBarPreview() {
    PointsAppTheme {
        TopApplicationBar(
            title = stringResource(id = R.string.chartScreenHeader),
            onNavigateBack = {},
            onSaveChartToFile = {},
            modifier = Modifier,
        )
    }
}
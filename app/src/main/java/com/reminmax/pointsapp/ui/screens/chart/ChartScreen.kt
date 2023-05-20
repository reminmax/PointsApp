package com.reminmax.pointsapp.ui.screens.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reminmax.pointsapp.R
import com.reminmax.pointsapp.ui.MainViewModel
import com.reminmax.pointsapp.ui.screens.chart.components.TopApplicationBar
import com.reminmax.pointsapp.ui.shared.AppSnackBarHost
import com.reminmax.pointsapp.ui.theme.PointsAppTheme
import com.reminmax.pointsapp.ui.theme.spacing

@Composable
fun ChartRoute(
    viewModel: MainViewModel,
    snackBarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ChartScreen(
        snackBarHostState = snackBarHostState,
        onNavigateBack = onNavigateBack,
        modifier = Modifier,
    )
}

@Composable
fun ChartScreen(
    snackBarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { AppSnackBarHost(hostState = snackBarHostState) },
        topBar = {
            TopApplicationBar(
                modifier = Modifier,
                title = stringResource(id = R.string.chartScreenHeader),
                onNavigateBack = onNavigateBack,
            )
        },
    ) { innerPadding ->
        ChartScreenContent(
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun ChartScreenContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = MaterialTheme.spacing.large,
                end = MaterialTheme.spacing.large,
                top = MaterialTheme.spacing.small,
            )
    ) {
        Text(text = "Chart screen")
    }
}

@Composable
@Preview(showBackground = true)
fun ChartScreenPreview() {
    PointsAppTheme {
        ChartScreen(
            snackBarHostState = SnackbarHostState(),
            onNavigateBack = {}
        )
    }
}
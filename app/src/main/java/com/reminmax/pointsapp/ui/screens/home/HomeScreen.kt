package com.reminmax.pointsapp.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import com.reminmax.pointsapp.ui.screens.home.components.PointCountTextField
import com.reminmax.pointsapp.ui.shared.AppSnackBarHost
import com.reminmax.pointsapp.ui.shared.observeWithLifecycle
import com.reminmax.pointsapp.ui.theme.PointsAppTheme
import com.reminmax.pointsapp.ui.theme.spacing

@Composable
fun HomeRoute(
    viewModel: MainViewModel,
    snackBarHostState: SnackbarHostState,
    onNavigateToChartScreen: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.eventsFlow.observeWithLifecycle { event ->
        when (event) {
            HomeScreenEvent.NavigateToChartScreen -> onNavigateToChartScreen()
        }
    }

    HomeScreen(
        snackBarHostState = snackBarHostState,
        pointCount = uiState.pointCount,
        onPointCountValueChanged = viewModel::onPointCountValueChanged,
        onPointCountValueCleared = viewModel::onPointCountValueCleared,
        isLoading = uiState.isLoading,
        onNavigateToChartScreen = onNavigateToChartScreen,
        onGetPoints = viewModel::getPoints,
        isGoButtonAvailable = uiState.isGoButtonAvailable,
        errorMessage = uiState.pointCountError,
        modifier = Modifier,
    )
}

@Composable
fun HomeScreen(
    snackBarHostState: SnackbarHostState,
    pointCount: String,
    onPointCountValueChanged: (String) -> Unit,
    onPointCountValueCleared: () -> Unit,
    onNavigateToChartScreen: () -> Unit,
    onGetPoints: () -> Unit,
    isGoButtonAvailable: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier,
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { AppSnackBarHost(hostState = snackBarHostState) },
    ) { innerPadding ->
        HomeScreenContent(
            pointCount = pointCount,
            onPointCountValueChanged = onPointCountValueChanged,
            onPointCountValueCleared = onPointCountValueCleared,
            onGetPoints = onGetPoints,
            onNavigateToChartScreen = onNavigateToChartScreen,
            isGoButtonAvailable = isGoButtonAvailable,
            errorMessage = errorMessage,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun HomeScreenContent(
    pointCount: String,
    isGoButtonAvailable: Boolean,
    onPointCountValueChanged: (String) -> Unit,
    onPointCountValueCleared: () -> Unit,
    onGetPoints: () -> Unit,
    onNavigateToChartScreen: () -> Unit,
    errorMessage: String?,
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
        Text(
            text = stringResource(id = R.string.homeScreenInfo),
            style = MaterialTheme.typography.body1,
        )
        PointCountTextField(
            value = pointCount,
            onValueChange = onPointCountValueChanged,
            onClearValue = onPointCountValueCleared,
            onDone = {},
            errorMessage = errorMessage,
            modifier = Modifier.padding(top = MaterialTheme.spacing.large)
        )
        Button(
            //onClick = onNavigateToChartScreen,
            onClick = onGetPoints,
            shape = MaterialTheme.shapes.small,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.small),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onPrimary,
            ),
            enabled = isGoButtonAvailable
        ) {
            Text(
                text = stringResource(id = R.string.goButtonLabel),
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    PointsAppTheme {
        HomeScreen(
            snackBarHostState = SnackbarHostState(),
            isLoading = false,
            onNavigateToChartScreen = {},
            isGoButtonAvailable = false,
            pointCount = "10",
            onPointCountValueChanged = {},
            onPointCountValueCleared = {},
            onGetPoints = {},
            errorMessage = ""
        )
    }
}
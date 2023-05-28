package com.reminmax.pointsapp.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.reminmax.pointsapp.domain.model.Point
import com.reminmax.pointsapp.ui.screens.home.components.PointCountTextField
import com.reminmax.pointsapp.ui.shared.AppSnackBarHost
import com.reminmax.pointsapp.ui.shared.LoadingButton
import com.reminmax.pointsapp.ui.shared.observeWithLifecycle
import com.reminmax.pointsapp.ui.theme.PointsAppTheme
import com.reminmax.pointsapp.ui.theme.spacing
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    snackBarHostState: SnackbarHostState,
    onNavigateToChartScreen: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.eventsFlow.observeWithLifecycle { event ->
        when (event) {
            is HomeScreenEvent.NavigateToChartScreen ->
                onNavigateToChartScreen(
                    Json.encodeToString(ListSerializer(Point.serializer()), event.points)
                )
        }
    }

    HomeScreen(
        snackBarHostState = snackBarHostState,
        pointCount = uiState.pointCount,
        isLoading = uiState.isLoading,
        isGoButtonAvailable = uiState.isGoButtonAvailable,
        errorMessage = uiState.errorMessage,
        dispatchAction = viewModel::dispatch
    )
}

@Composable
fun HomeScreen(
    snackBarHostState: SnackbarHostState,
    pointCount: String,
    isGoButtonAvailable: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    dispatchAction: (HomeAction) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { AppSnackBarHost(hostState = snackBarHostState) },
    ) { innerPadding ->
        HomeScreenContent(
            pointCount = pointCount,
            isGoButtonAvailable = isGoButtonAvailable,
            errorMessage = errorMessage,
            isLoading = isLoading,
            dispatchAction = dispatchAction,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun HomeScreenContent(
    pointCount: String,
    isGoButtonAvailable: Boolean,
    errorMessage: String?,
    isLoading: Boolean,
    dispatchAction: (HomeAction) -> Unit,
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
            errorMessage = errorMessage,
            dispatchAction = dispatchAction,
            modifier = Modifier.padding(top = MaterialTheme.spacing.large)
        )

        LoadingButton(
            onClick = {
                dispatchAction(HomeAction.GetPoints)
            },
            modifier = modifier.fillMaxWidth(),
            enabled = isGoButtonAvailable && !isLoading,
            loading = isLoading,
            content = {
                Text(
                    text = stringResource(id = R.string.goButtonLabel),
                    style = MaterialTheme.typography.button
                )
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    PointsAppTheme {
        HomeScreen(
            snackBarHostState = SnackbarHostState(),
            isLoading = false,
            isGoButtonAvailable = false,
            pointCount = "10",
            errorMessage = null,
            dispatchAction = {}
        )
    }
}
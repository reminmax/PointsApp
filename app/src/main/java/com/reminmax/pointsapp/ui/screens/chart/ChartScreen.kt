package com.reminmax.pointsapp.ui.screens.chart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reminmax.pointsapp.R
import com.reminmax.pointsapp.domain.model.Point
import com.reminmax.pointsapp.ui.MainViewModel
import com.reminmax.pointsapp.ui.screens.chart.components.PointsChart
import com.reminmax.pointsapp.ui.screens.chart.components.PointsGrid
import com.reminmax.pointsapp.ui.screens.chart.components.TopApplicationBar
import com.reminmax.pointsapp.ui.shared.AppSnackBarHost
import com.reminmax.pointsapp.ui.shared.WindowInfo
import com.reminmax.pointsapp.ui.shared.rememberWindowInfo
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
        points = uiState.points,
        snackBarHostState = snackBarHostState,
        onNavigateBack = onNavigateBack,
        modifier = Modifier,
    )
}

@Composable
fun ChartScreen(
    points: List<Point>,
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
            points = points,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun ChartScreenContent(
    points: List<Point>,
    modifier: Modifier = Modifier,
) {
    val windowInfo = rememberWindowInfo()
    if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
        ChartScreenContentVertical(
            points = points,
            modifier = modifier
        )
    } else {
        ChartScreenContentHorizontal(
            points = points,
            modifier = modifier
        )
    }
}

@Composable
fun ChartScreenContentVertical(
    points: List<Point>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.large),
    ) {
        PointsGrid(
            points = points,
            modifier = Modifier.fillMaxWidth()
        )
        PointsChart(
            points = points,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = MaterialTheme.spacing.medium)
        )
    }
}

@Composable
fun ChartScreenContentHorizontal(
    points: List<Point>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.large),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PointsGrid(
            points = points,
            //modifier = Modifier.fillMaxHeight()
            modifier = Modifier.weight(1f)
        )
        PointsChart(
            points = points,
            //modifier = Modifier.fillMaxHeight()
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ChartScreenPreview() {
    PointsAppTheme {
        ChartScreen(
            points = listOf(
                Point(x = 10.0f, y = 20.0f),
            ),
            snackBarHostState = SnackbarHostState(),
            onNavigateBack = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ChartScreenContentVerticalPreview() {
    PointsAppTheme {
        ChartScreenContentVertical(
            points = listOf(
                Point(x = 10.0f, y = 20.0f),
            ),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ChartScreenContentHorizontalPreview() {
    PointsAppTheme {
        ChartScreenContentHorizontal(
            points = listOf(
                Point(x = 10.0f, y = 20.0f),
            ),
        )
    }
}
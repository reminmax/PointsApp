package com.reminmax.pointsapp.ui.screens.chart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reminmax.pointsapp.R
import com.reminmax.pointsapp.domain.model.LinearChartStyle
import com.reminmax.pointsapp.domain.model.Point
import com.reminmax.pointsapp.ui.MainViewModel
import com.reminmax.pointsapp.ui.screens.chart.components.ChartStyleOptions
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
        chartStyle = uiState.chartStyle,
        onChartStyleSelected = viewModel::onChartStyleSelected
    )
}

@Composable
fun ChartScreen(
    points: List<Point>,
    snackBarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
    chartStyle: LinearChartStyle,
    onChartStyleSelected: (LinearChartStyle) -> Unit,
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
            onChartStyleSelected = onChartStyleSelected,
            chartStyle = chartStyle,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun ChartScreenContent(
    points: List<Point>,
    onChartStyleSelected: (LinearChartStyle) -> Unit,
    chartStyle: LinearChartStyle,
    modifier: Modifier = Modifier,
) {
    val windowInfo = rememberWindowInfo()
    if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
        ChartScreenContentVertical(
            points = points,
            screenHeight = windowInfo.screenHeight,
            onChartStyleSelected = onChartStyleSelected,
            chartStyle = chartStyle,
            modifier = modifier
        )
    } else {
        ChartScreenContentHorizontal(
            points = points,
            chartStyle = chartStyle,
            onChartStyleSelected = onChartStyleSelected,
            modifier = modifier
        )
    }
}

@Composable
fun ChartScreenContentVertical(
    points: List<Point>,
    screenHeight: Dp,
    onChartStyleSelected: (LinearChartStyle) -> Unit,
    chartStyle: LinearChartStyle,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.large),
    ) {
        PointsGrid(
            points = points,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = screenHeight / 3)
                .wrapContentHeight(align = Alignment.Top)
        )

        ChartStyleOptions(
            onChartStyleSelected = onChartStyleSelected,
            chartStyle = chartStyle,
            modifier = Modifier
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = MaterialTheme.spacing.medium)
                .clipToBounds()
        ) {
            PointsChart(
                points = points,
                chartStyle = chartStyle,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ChartScreenContentHorizontal(
    points: List<Point>,
    chartStyle: LinearChartStyle,
    onChartStyleSelected: (LinearChartStyle) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.large),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            PointsGrid(
                points = points,
                modifier = Modifier.weight(1f)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            ChartStyleOptions(
                onChartStyleSelected = onChartStyleSelected,
                chartStyle = chartStyle,
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    //.weight(1f)
                    .clipToBounds()
            ) {
                PointsChart(
                    points = points,
                    chartStyle = chartStyle,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
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
            chartStyle = LinearChartStyle.DEFAULT,
            onChartStyleSelected = {},
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
            screenHeight = 200.dp,
            onChartStyleSelected = {},
            chartStyle = LinearChartStyle.DEFAULT,
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
            chartStyle = LinearChartStyle.DEFAULT,
            onChartStyleSelected = {},
        )
    }
}
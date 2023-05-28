package com.reminmax.pointsapp.ui.screens.chart.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kpstv.compose.kapture.ScreenshotController
import com.kpstv.compose.kapture.attachController
import com.kpstv.compose.kapture.rememberScreenshotController
import com.reminmax.pointsapp.domain.model.LinearChartStyle
import com.reminmax.pointsapp.domain.model.Point
import com.reminmax.pointsapp.ui.theme.PointsAppTheme
import com.reminmax.pointsapp.ui.theme.spacing

@Composable
fun ChartScreenContentHorizontal(
    points: List<Point>,
    chartStyle: LinearChartStyle,
    onChartStyleSelected: (LinearChartStyle) -> Unit,
    screenshotController: ScreenshotController,
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
                    .clipToBounds()
                    .attachController(screenshotController)
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
fun ChartScreenContentHorizontalPreview() {
    PointsAppTheme {
        ChartScreenContentHorizontal(
            points = listOf(
                Point(x = 10.0f, y = 20.0f),
            ),
            chartStyle = LinearChartStyle.DEFAULT,
            onChartStyleSelected = {},
            screenshotController = rememberScreenshotController()
        )
    }
}
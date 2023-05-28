package com.reminmax.pointsapp.ui.screens.chart.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kpstv.compose.kapture.ScreenshotController
import com.kpstv.compose.kapture.attachController
import com.kpstv.compose.kapture.rememberScreenshotController
import com.reminmax.pointsapp.domain.model.LinearChartStyle
import com.reminmax.pointsapp.domain.model.Point
import com.reminmax.pointsapp.ui.theme.PointsAppTheme
import com.reminmax.pointsapp.ui.theme.spacing

@Composable
fun ChartScreenContentVertical(
    points: List<Point>,
    screenHeight: Dp,
    onChartStyleSelected: (LinearChartStyle) -> Unit,
    chartStyle: LinearChartStyle,
    screenshotController: ScreenshotController,
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
            screenshotController = rememberScreenshotController()
        )
    }
}

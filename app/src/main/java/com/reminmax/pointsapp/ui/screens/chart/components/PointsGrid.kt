package com.reminmax.pointsapp.ui.screens.chart.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reminmax.pointsapp.domain.model.Point
import com.reminmax.pointsapp.ui.theme.PointsAppTheme
import com.reminmax.pointsapp.ui.theme.spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PointsGrid(
    points: List<Point>,
    modifier: Modifier = Modifier,
    column1Weight: Float = .20f,
    column2Weight: Float = .40f,
    column3Weight: Float = .40f,
) {
    LazyColumn(
        modifier = modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.primary
            )
    ) {
        stickyHeader {
            Row(
                Modifier.background(
                    color = MaterialTheme.colors.secondary.copy(alpha = 0.9F)
                )
            ) {
                TableCell(text = "index", weight = column1Weight)
                TableCell(text = "x", weight = column2Weight)
                TableCell(text = "y", weight = column3Weight)
            }
        }
        items(points.size) { index ->
            Row(Modifier.fillMaxWidth()) {
                TableCell(
                    text = "$index",
                    weight = column1Weight
                )
                TableCell(
                    text = "${points[index].x}",
                    weight = column2Weight
                )
                TableCell(
                    text = "${points[index].y}",
                    weight = column3Weight
                )
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary
            )
            .weight(weight)
            .padding(MaterialTheme.spacing.extraSmall),
        textAlign = TextAlign.Center
    )
}

@Composable
@Preview(showBackground = true)
fun PointsGridPreview() {
    PointsAppTheme {
        PointsGrid(
            points = listOf(
                Point(
                    x = 10.0f,
                    y = 20.0f
                )
            )
        )
    }
}
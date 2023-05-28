package com.reminmax.pointsapp.ui.screens.chart.components

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.reminmax.pointsapp.domain.model.LinearChartStyle
import com.reminmax.pointsapp.domain.model.Point
import com.reminmax.pointsapp.ui.shared.dashedBorder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PointsChart(
    points: List<Point>,
    modifier: Modifier = Modifier,
    chartStyle: LinearChartStyle,
) {
    val minX = remember { points.minBy { it.x }.x }
    val maxX = remember { points.maxBy { it.x }.x }
    val minY = remember { points.minBy { it.y }.y }
    val maxY = remember { points.maxBy { it.y }.y }

    val primaryColor = MaterialTheme.colors.primary

    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val transformableState = rememberTransformableState { scaleChange, offsetChange, _ ->
        scale *= scaleChange
        offsetX += offsetChange.x
        offsetY += offsetChange.y
    }

    Canvas(
        modifier = modifier
            .background(color = Color(0xFFFCFCFA))
            .dashedBorder(
                strokeWidth = 1.dp,
                color = Color(0xFFEEEEE4),
            )
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
            )
            .transformable(state = transformableState)
            .offset(x = offsetX.pxToDp(), y = offsetY.pxToDp())
            .combinedClickable(
                onClick = { },
                onLongClick = {
                    scale = 1f
                    offsetX = 0f
                    offsetY = 0f
                },
            )
    ) {

        val canvasWidth = size.width
        val canvasHeight = size.height

        val horizontalUnitWidth = canvasWidth / (maxX.normalize() + minX.normalize())
        val verticalUnitWidth = canvasHeight / (maxY.normalize() + minY.normalize())

        // Origin point (0,0)
        val originPoint = PointF(
            if (minX < 0) horizontalUnitWidth * minX.normalize() else 0f,
            if (minY < 0) verticalUnitWidth * maxY.normalize() else canvasHeight
        )

        // vertical axis
        drawLine(
            start = Offset(x = originPoint.x, y = 0f),
            end = Offset(x = originPoint.x, y = canvasHeight),
            color = Color.Black,
            strokeWidth = 2.dp.toPx()
        )

        // horizontal axis
        drawLine(
            start = Offset(x = 0f, y = originPoint.y),
            end = Offset(x = canvasWidth, y = originPoint.y),
            color = Color.Black,
            strokeWidth = 2.dp.toPx()
        )

        // Points coordinates calculation
        val pointsToDraw = points
            .sortedBy { it.x }
            .map { point ->
                Offset(
                    x = originPoint.x + point.x * horizontalUnitWidth,
                    y = originPoint.y - point.y * verticalUnitWidth
                )
            }

        // Draw the points and the lines
        pointsToDraw.forEachIndexed { index, offset ->
            // Points
            drawCircle(
                brush = SolidColor(primaryColor),
                center = offset,
                radius = 4.dp.toPx(),
                style = Fill
            )

            // Lines
            when (chartStyle) {
                LinearChartStyle.DEFAULT -> {
                    if (pointsToDraw.size >= index + 2) {
                        drawLine(
                            start = offset,
                            end = pointsToDraw[index + 1],
                            color = primaryColor,
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }

                LinearChartStyle.SMOOTH -> {
                    if (pointsToDraw.size >= index + 2) {
                        val endPoint = pointsToDraw[index + 1]
                        val controlPoint1 = Offset(
                            offset.x + (endPoint.x - offset.x) / 2f,
                            offset.y
                        )
                        val controlPoint2 =
                            Offset(offset.x + (endPoint.x - offset.x) / 2f, endPoint.y)

                        drawPath(
                            path = Path().apply {
                                moveTo(offset.x, offset.y)
                                cubicTo(
                                    controlPoint1.x,
                                    controlPoint1.y,
                                    controlPoint2.x,
                                    controlPoint2.y,
                                    endPoint.x,
                                    endPoint.y
                                )
                            },
                            color = primaryColor,
                            style = Stroke(width = 1f)
                        )
                    }
                }
            }
        }
    }
}

private fun Float.normalize(): Float =
    if (this < 0) -this else this

@Composable
private fun Float.pxToDp() = with(LocalDensity.current) {
    this@pxToDp.toDp()
}

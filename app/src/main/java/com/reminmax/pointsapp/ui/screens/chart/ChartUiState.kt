package com.reminmax.pointsapp.ui.screens.chart

import com.reminmax.pointsapp.domain.model.LinearChartStyle
import com.reminmax.pointsapp.domain.model.Point

data class ChartUiState(
    val points: List<Point> = listOf(),
    val chartStyle: LinearChartStyle = LinearChartStyle.DEFAULT,
)
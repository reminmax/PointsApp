package com.reminmax.pointsapp.ui

import com.reminmax.pointsapp.domain.model.LinearChartStyle
import com.reminmax.pointsapp.domain.model.Point

data class MainUiState(
    val pointCount: String = "",
    val points: List<Point> = listOf(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val chartStyle: LinearChartStyle = LinearChartStyle.DEFAULT
) {
    val isGoButtonAvailable: Boolean
        get() = pointCount.isNotBlank()

    val isPointCountValid: Boolean
        get() = errorMessage == null
}
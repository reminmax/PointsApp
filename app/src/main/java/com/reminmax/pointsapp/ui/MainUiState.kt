package com.reminmax.pointsapp.ui

import com.reminmax.pointsapp.domain.model.Point

data class MainUiState(
    val pointCount: String = "",
    val pointCountError: String? = null,
    val isLoading: Boolean = false,
    val points: List<Point> = listOf(),
    val errorMessage: String? = null,
) {
    val isGoButtonAvailable: Boolean
        get() = pointCount.isNotBlank()

    val isPointCountValid: Boolean
        get() = pointCountError == null
}
package com.reminmax.pointsapp.ui

import com.reminmax.pointsapp.domain.model.Point

data class MainUiState(
    val pointCount: String = "",
    val pointCountError: String? = null,
    val isLoading: Boolean = false,
    val isPointCountValueValid: Boolean = false,
    val points: List<Point> = listOf(),
    val errorMessage: String? = null,
)
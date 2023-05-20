package com.reminmax.pointsapp.ui

data class MainUiState(
    val pointCount: String = "",
    val pointCountError: String? = null,
    val isLoading: Boolean = false,
    val isPointCountValueValid: Boolean = false,
)
package com.reminmax.pointsapp.ui.screens.home

data class HomeUiState(
    val pointCount: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val isGoButtonAvailable: Boolean
        get() = pointCount.isNotBlank()

    val isPointCountValid: Boolean
        get() = errorMessage == null
}
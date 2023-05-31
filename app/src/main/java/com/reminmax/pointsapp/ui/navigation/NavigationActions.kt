package com.reminmax.pointsapp.ui.navigation

import androidx.navigation.NavHostController

class NavigationActions(private val navController: NavHostController) {

    fun navigateToChartScreen(points: String) {
        navController.navigate(buildChartRoute(points = points))
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    private fun buildChartRoute(points: String): String =
        "${NavigationGraph.CHART}/$points"
}
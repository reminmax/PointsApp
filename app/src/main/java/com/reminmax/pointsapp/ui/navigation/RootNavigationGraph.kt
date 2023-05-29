package com.reminmax.pointsapp.ui.navigation

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.reminmax.pointsapp.ui.screens.chart.ChartRoute
import com.reminmax.pointsapp.ui.screens.chart.ChartViewModel
import com.reminmax.pointsapp.ui.screens.home.HomeRoute
import com.reminmax.pointsapp.ui.screens.home.HomeViewModel

@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    onGoToAppSettings: () -> Unit,
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    }
) {
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    NavHost(
        navController = navController,
        route = NavigationGraph.ROOT,
        startDestination = NavigationGraph.HOME
    ) {

        // Home screen
        composable(route = NavigationGraph.HOME) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeRoute(
                viewModel = viewModel,
                snackBarHostState = snackBarHostState,
                onNavigateToChartScreen = { points ->
                    navActions.navigateToChartScreen(points = points)
                }
            )
        }

        // Chart screen
        composable(
            route = buildChartScreenRoute(),
            arguments = listOf(navArgument(NavigationParams.POINTS) { type = NavType.StringType })
        ) {
            val viewModel = hiltViewModel<ChartViewModel>()
            ChartRoute(
                viewModel = viewModel,
                snackBarHostState = snackBarHostState,
                onNavigateBack = {
                    navActions.navigateBack()
                },
                onGoToAppSettings = onGoToAppSettings,
            )
        }
    }
}

object NavigationGraph {
    const val ROOT = "rootGraph"
    const val HOME = "homeGraph"
    const val CHART = "chartGraph"
}

object NavigationParams {
    const val POINTS = "points"
}

private fun buildChartScreenRoute() =
    "${NavigationGraph.CHART}/{${NavigationParams.POINTS}}"
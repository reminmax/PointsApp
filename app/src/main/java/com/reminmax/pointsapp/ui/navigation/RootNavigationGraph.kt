package com.reminmax.pointsapp.ui.navigation

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.reminmax.pointsapp.ui.MainViewModel
import com.reminmax.pointsapp.ui.screens.chart.ChartRoute
import com.reminmax.pointsapp.ui.screens.home.HomeRoute

@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    onGoToAppSettings: () -> Unit,
) {
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val viewModel = hiltViewModel<MainViewModel>()

    NavHost(
        navController = navController,
        route = NavigationGraph.ROOT,
        startDestination = NavigationGraph.HOME
    ) {

        // Home screen
        composable(route = NavigationGraph.HOME) {
            HomeRoute(
                viewModel = viewModel,
                snackBarHostState = snackBarHostState,
                onNavigateToChartScreen = {
                    navController.navigate(NavigationGraph.CHART)
                }
            )
        }

        // Chart screen
        composable(route = NavigationGraph.CHART) {
            ChartRoute(
                viewModel = viewModel,
                snackBarHostState = snackBarHostState,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

object NavigationGraph {
    const val ROOT = "rootGraph"
    const val HOME = "homeGraph"
    const val CHART = "chartGraph"
}
package com.reminmax.pointsapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.reminmax.pointsapp.ui.navigation.RootNavigationGraph
import com.reminmax.pointsapp.ui.theme.PointsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PointsAppTheme {
                RootNavigationGraph(
                    navController = rememberNavController(),
                )
            }
        }
    }
}
package com.reminmax.pointsapp.ui.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val COMPACT_SCREEN_WIDTH = 600
private const val MEDIUM_SCREEN_WIDTH = 840
private const val COMPACT_SCREEN_HEIGHT = 480
private const val MEDIUM_SCREEN_HEIGHT = 900

@Composable
fun rememberWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    return WindowInfo(
        screenWidthInfo = when {
            configuration.screenWidthDp < COMPACT_SCREEN_WIDTH -> WindowInfo.WindowType.Compact
            configuration.screenWidthDp < MEDIUM_SCREEN_WIDTH -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Expanded
        },
        screenHeightInfo = when {
            configuration.screenHeightDp < COMPACT_SCREEN_HEIGHT -> WindowInfo.WindowType.Compact
            configuration.screenHeightDp < MEDIUM_SCREEN_HEIGHT -> WindowInfo.WindowType.Medium
            else -> WindowInfo.WindowType.Expanded
        },
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp
    )
}

data class WindowInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenWidth: Dp,
    val screenHeight: Dp
) {
    sealed class WindowType {
        object Compact: WindowType()
        object Medium: WindowType()
        object Expanded: WindowType()
    }
}
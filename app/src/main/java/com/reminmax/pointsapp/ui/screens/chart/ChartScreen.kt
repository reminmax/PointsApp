package com.reminmax.pointsapp.ui.screens.chart

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kpstv.compose.kapture.ScreenshotController
import com.kpstv.compose.kapture.rememberScreenshotController
import com.reminmax.pointsapp.R
import com.reminmax.pointsapp.domain.model.LinearChartStyle
import com.reminmax.pointsapp.domain.model.Point
import com.reminmax.pointsapp.ui.screens.chart.components.ChartScreenContentHorizontal
import com.reminmax.pointsapp.ui.screens.chart.components.ChartScreenContentVertical
import com.reminmax.pointsapp.ui.screens.chart.components.TopApplicationBar
import com.reminmax.pointsapp.ui.shared.AppSnackBarHost
import com.reminmax.pointsapp.ui.shared.WindowInfo
import com.reminmax.pointsapp.ui.shared.observeWithLifecycle
import com.reminmax.pointsapp.ui.shared.rememberWindowInfo
import com.reminmax.pointsapp.ui.theme.PointsAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileNotFoundException

private const val displayName = "ChartImage"

@Composable
fun ChartRoute(
    viewModel: ChartViewModel,
    snackBarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val screenshotController = rememberScreenshotController()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    ChartScreen(
        points = uiState.points,
        snackBarHostState = snackBarHostState,
        onNavigateBack = onNavigateBack,
        chartStyle = uiState.chartStyle,
        dispatchAction = viewModel::dispatch,
        screenshotController = screenshotController,
    )

    viewModel.eventsFlow.observeWithLifecycle { event ->
        when (event) {
            is ChartScreenEvent.SaveChartToFile -> {
                coroutineScope.launch(Dispatchers.IO) {
                    captureCanvasToBitmap(
                        context = context,
                        screenshotController = screenshotController
                    )
                }
            }
        }
    }
}

@Composable
fun ChartScreen(
    points: List<Point>,
    snackBarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
    chartStyle: LinearChartStyle,
    dispatchAction: (ChartAction) -> Unit,
    screenshotController: ScreenshotController,
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { AppSnackBarHost(hostState = snackBarHostState) },
        topBar = {
            TopApplicationBar(
                modifier = Modifier,
                title = stringResource(id = R.string.chartScreenHeader),
                onNavigateBack = onNavigateBack,
                onSaveChartToFile = {
                    dispatchAction(ChartAction.SaveChartToFile)
                },
            )
        },
    ) { innerPadding ->
        ChartScreenContent(
            points = points,
            chartStyle = chartStyle,
            dispatchAction = dispatchAction,
            screenshotController = screenshotController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun ChartScreenContent(
    points: List<Point>,
    chartStyle: LinearChartStyle,
    dispatchAction: (ChartAction) -> Unit,
    screenshotController: ScreenshotController,
    modifier: Modifier = Modifier,
) {
    val windowInfo = rememberWindowInfo()
    if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
        ChartScreenContentVertical(
            points = points,
            screenHeight = windowInfo.screenHeight,
            onChartStyleSelected = { newStyle ->
                dispatchAction(ChartAction.ChartStyleSelected(newStyle))
            },
            chartStyle = chartStyle,
            screenshotController = screenshotController,
            modifier = modifier
        )
    } else {
        ChartScreenContentHorizontal(
            points = points,
            chartStyle = chartStyle,
            onChartStyleSelected = { newStyle ->
                dispatchAction(ChartAction.ChartStyleSelected(newStyle))
            },
            screenshotController = screenshotController,
            modifier = modifier
        )
    }
}

private suspend fun captureCanvasToBitmap(
    context: Context,
    screenshotController: ScreenshotController
) {
    screenshotController.captureToBitmap(
        config = Bitmap.Config.ARGB_8888
    ).onSuccess { bitmap ->
        val uri = saveImageToMediaStore(
            context = context,
            bitmap = bitmap
        )
        println(uri)
    }.onFailure {
        // TODO(Display error message)
        println(it.localizedMessage)
    }
}

private fun saveImageToMediaStore(
    context: Context,
    bitmap: Bitmap
): Uri? {
    val imageCollections = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val imageDetails = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val resolver = context.applicationContext.contentResolver
    val imageContentUri = resolver.insert(imageCollections, imageDetails) ?: return null

    return try {
        resolver.openOutputStream(imageContentUri, "w").use { os ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageDetails.clear()
            imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(imageContentUri, imageDetails, null, null)
        }

        imageContentUri
    } catch (e: FileNotFoundException) {
        // Some legacy devices won't create directory for the Uri if dir not exist, resulting in
        // a FileNotFoundException. To resolve this issue, we should use the File API to save the
        // image, which allows us to create the directory ourselves.
        null
    }
}

@Composable
@Preview(showBackground = true)
fun ChartScreenPreview() {
    PointsAppTheme {
        ChartScreen(
            points = listOf(
                Point(x = 10.0f, y = 20.0f),
            ),
            snackBarHostState = SnackbarHostState(),
            onNavigateBack = {},
            chartStyle = LinearChartStyle.DEFAULT,
            dispatchAction = {},
            screenshotController = rememberScreenshotController(),
        )
    }
}
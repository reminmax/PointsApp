package com.reminmax.pointsapp.ui.screens.chart

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.kpstv.compose.kapture.ScreenshotController
import com.kpstv.compose.kapture.rememberScreenshotController
import com.reminmax.pointsapp.R
import com.reminmax.pointsapp.common.util.isSdkVer29OrLater
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

private const val FILE_DISPLAY_NAME = "ChartImage"
private const val WRITE_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ChartRoute(
    viewModel: ChartViewModel,
    snackBarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
    onGoToAppSettings: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userMessages by viewModel.userMessages.collectAsStateWithLifecycle()
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

    val permissionState = rememberPermissionState(WRITE_EXTERNAL_STORAGE_PERMISSION)
    val fileSavedSuccessfully = stringResource(id = R.string.fileSavedSuccessfully)
    val captureCanvasToBitmapError = stringResource(id = R.string.captureCanvasToBitmapError)
    val saveImageToMediaStoreError = stringResource(id = R.string.saveImageToMediaStoreError)
    val goToAppSettings = stringResource(id = R.string.goToAppSettings)
    val rationaleText = stringResource(id = R.string.rationaleText)

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                coroutineScope.launch(Dispatchers.IO) {
                    captureCanvasToBitmap(
                        context = context,
                        screenshotController = screenshotController,
                        dispatchAction = viewModel::dispatch,
                        fileSavedSuccessfully = fileSavedSuccessfully,
                        captureCanvasToBitmapError = captureCanvasToBitmapError,
                        saveImageToMediaStoreError = saveImageToMediaStoreError,
                    )
                }
            }
        }

    // Screen events
    viewModel.eventsFlow.observeWithLifecycle { event ->
        when (event) {
            is ChartScreenEvent.SaveChartToFile -> {
                if (permissionState.status.isGranted || isSdkVer29OrLater()) {
                    coroutineScope.launch(Dispatchers.IO) {
                        captureCanvasToBitmap(
                            context = context,
                            screenshotController = screenshotController,
                            dispatchAction = viewModel::dispatch,
                            fileSavedSuccessfully = fileSavedSuccessfully,
                            captureCanvasToBitmapError = captureCanvasToBitmapError,
                            saveImageToMediaStoreError = saveImageToMediaStoreError,
                        )
                    }
                } else {
                    if (permissionState.status.shouldShowRationale) {
                        viewModel.dispatch(
                            ChartAction.ShowUserMessage(
                                messageToShow = rationaleText,
                                actionLabel = goToAppSettings,
                                onActionPerformed = {
                                    onGoToAppSettings()
                                }
                            )
                        )
                    } else {
                        permissionLauncher.launch(WRITE_EXTERNAL_STORAGE_PERMISSION)
                    }
                }
            }
        }
    }

    // User messages
    if (userMessages.isNotEmpty()) {
        val message = remember(uiState) { userMessages[0] }
        val messageText: String = message.message
        LaunchedEffect(messageText, snackBarHostState) {
            val snackBarResult = snackBarHostState.showSnackbar(
                message = messageText,
                actionLabel = message.actionLabel
            )
            if (snackBarResult == SnackbarResult.ActionPerformed) {
                message.onActionPerformed()
            }
            viewModel.dispatch(ChartAction.UserMessageShown(message.id))
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
    screenshotController: ScreenshotController,
    dispatchAction: (ChartAction) -> Unit,
    fileSavedSuccessfully: String,
    captureCanvasToBitmapError: String,
    saveImageToMediaStoreError: String,
) {
    screenshotController.captureToBitmap(
        config = Bitmap.Config.ARGB_8888
    ).onSuccess { bitmap ->
        val uri = saveImageToMediaStore(
            context = context,
            bitmap = bitmap
        )
        dispatchAction(
            ChartAction.ShowUserMessage(
                messageToShow = if (uri != null) {
                    "$fileSavedSuccessfully: $uri"
                } else {
                    saveImageToMediaStoreError
                }
            )
        )
    }.onFailure {
        dispatchAction(
            ChartAction.ShowUserMessage(
                it.localizedMessage ?: captureCanvasToBitmapError
            )
        )
    }
}

private fun saveImageToMediaStore(
    context: Context,
    bitmap: Bitmap
): Uri? {
    val imageCollections = if (isSdkVer29OrLater()) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val imageDetails = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, FILE_DISPLAY_NAME)
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

        if (isSdkVer29OrLater()) {
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
package com.reminmax.pointsapp.ui

import androidx.lifecycle.viewModelScope
import com.reminmax.pointsapp.R
import com.reminmax.pointsapp.data.SResult
import com.reminmax.pointsapp.di.IoDispatcher
import com.reminmax.pointsapp.domain.helpers.INetworkUtils
import com.reminmax.pointsapp.domain.resource_provider.IResourceProvider
import com.reminmax.pointsapp.domain.use_case.IGetPointsUseCase
import com.reminmax.pointsapp.domain.use_case.IValidatePointCountUseCase
import com.reminmax.pointsapp.ui.base.BaseViewModel
import com.reminmax.pointsapp.ui.screens.home.HomeScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPointsUseCase: IGetPointsUseCase,
    private val resourceProvider: IResourceProvider,
    private val validatePointCountUseCase: IValidatePointCountUseCase,
    private val networkUtils: INetworkUtils,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun getPoints() {
        if (!networkUtils.hasNetworkConnection()) {
            showErrorMessage(
                message = resourceProvider.getString(R.string.noInternetConnection)
            )
            return
        }

        validatePointCountValue()
        if (!_uiState.value.isPointCountValid) return

        updatePoints(count = _uiState.value.pointCount.toInt())
    }

    private fun updatePoints(count: Int) {
        getPointsUseCase(count = count).onEach { result ->
            when (result) {
                is SResult.Success -> {
                    stopLoading()
                    _uiState.update {
                        it.copy(points = result.data)
                    }
                    sendEvent(HomeScreenEvent.NavigateToChartScreen)
                }

                is SResult.Error -> {
                    stopLoading()
                    showErrorMessage(message = result.message)
                }

                SResult.Empty -> {
                    stopLoading()
                    showErrorMessage(resourceProvider.getString(R.string.emptyResultErrorText))
                }

                SResult.Loading ->
                    startLoading()
            }
        }.launchIn(viewModelScope + ioDispatcher)
    }

    private fun validatePointCountValue() {
        val validationResult = validatePointCountUseCase(_uiState.value.pointCount)
        if (validationResult.successful) {
            _uiState.update {
                it.copy(errorMessage = null)
            }
        } else {
            _uiState.update {
                it.copy(errorMessage = validationResult.errorMessage)
            }
        }
    }

    fun onPointCountValueChanged(value: String) {
        _uiState.update {
            it.copy(pointCount = value)
        }
    }

    fun onPointCountValueCleared() {
        _uiState.update {
            it.copy(pointCount = "")
        }
    }

    private fun showErrorMessage(message: String? = null) {
        _uiState.update {
            it.copy(
                errorMessage = message
                    ?: resourceProvider.getString(R.string.anUnexpectedErrorOccurred)
            )
        }
    }

    private fun stopLoading() {
        _uiState.update {
            it.copy(isLoading = false)
        }
    }

    private fun startLoading() {
        _uiState.update {
            it.copy(isLoading = true)
        }
    }
}
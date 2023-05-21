package com.reminmax.pointsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reminmax.pointsapp.R
import com.reminmax.pointsapp.data.SResult
import com.reminmax.pointsapp.di.IoDispatcher
import com.reminmax.pointsapp.domain.resource_provider.IResourceProvider
import com.reminmax.pointsapp.domain.use_case.IGetPointsUseCase
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun getPoints() {
        getPointsUseCase(count = uiState.value.pointCount.toInt()).onEach { result ->
            when (result) {
                is SResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            points = result.data
                        )
                    }
                }
                is SResult.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                }
                SResult.Empty -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = resourceProvider.getString(R.string.emptyResultErrorText),
                            isLoading = false
                        )
                    }
                }
                SResult.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }.launchIn(viewModelScope + ioDispatcher)
    }

    fun onPointCountValueChanged(value: String) {
        _uiState.update {
            it.copy(
                pointCount = value,
                isPointCountValueValid = value.isNotBlank()
            )
        }
    }

    fun onPointCountValueCleared() {
        _uiState.update {
            it.copy(
                pointCount = "",
                isPointCountValueValid = false
            )
        }
    }
}
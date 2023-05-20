package com.reminmax.pointsapp.ui

import androidx.lifecycle.ViewModel
import com.reminmax.pointsapp.di.IoDispatcher
import com.reminmax.pointsapp.domain.resource_provider.IResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val resourceProvider: IResourceProvider,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

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
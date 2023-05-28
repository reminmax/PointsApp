package com.reminmax.pointsapp.ui.screens.chart

import androidx.lifecycle.SavedStateHandle
import com.reminmax.pointsapp.di.IoDispatcher
import com.reminmax.pointsapp.domain.model.LinearChartStyle
import com.reminmax.pointsapp.domain.model.Point
import com.reminmax.pointsapp.ui.base.BaseViewModel
import com.reminmax.pointsapp.ui.navigation.NavigationParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ChartUiState())
    val uiState: StateFlow<ChartUiState> = _uiState.asStateFlow()

    // Примечание:
    // Вопреки настятельной рекомендации Google не передавать сложные объекты в качестве
    // параметров навигации (вместо этого передавать ключ для извлечения данных),
    // https://developer.android.com/jetpack/compose/navigation#retrieving-complex-data
    // данная реализация состоит в передаче списка точек в формате json в качестве параметра
    // навигации.
    //
    // Данный подход считаю допустимым по двум причинам:
    // 1. Простота реализации (это не коммерческий проект); применение SharedViewModel,
    // создание базы данных, использование DataStore (Shared Preferences) и др. распространенных
    // способов передачи состояния нахожу избыточным.
    // 2. Максимально допустимый размер сохраненного состояния Android (1Mb) гарантированно не
    // будет превышен, так как максимальное количество точек ограничено диапазоном 1..100 путем
    // валидации введенного пользователем значения (класс Validator).
    init {
        savedStateHandle.get<String>(NavigationParams.POINTS)?.let { points ->
            _uiState.update {
                it.copy(points = Json.decodeFromString<List<Point>>(points))
            }
        }
    }

    fun onChartStyleSelected(style: LinearChartStyle) {
        _uiState.update {
            it.copy(chartStyle = style)
        }
    }

    fun saveChartToFile() {
        _uiState.update {
            it.copy(shouldSaveChartToFile = true)
        }
    }

    fun chartSavedToFile() {
        _uiState.update {
            it.copy(shouldSaveChartToFile = false)
        }
    }
}
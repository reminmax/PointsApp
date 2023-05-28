package com.reminmax.pointsapp.ui.screens.chart

import com.reminmax.pointsapp.domain.model.LinearChartStyle

sealed class ChartAction {
    data class ChartStyleSelected(val style: LinearChartStyle): ChartAction()
    object SaveChartToFile: ChartAction()
}
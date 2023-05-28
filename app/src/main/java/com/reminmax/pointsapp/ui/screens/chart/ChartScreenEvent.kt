package com.reminmax.pointsapp.ui.screens.chart

import com.reminmax.pointsapp.ui.base.IBaseEvent

sealed class ChartScreenEvent : IBaseEvent {
    object SaveChartToFile : ChartScreenEvent()
}
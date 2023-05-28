package com.reminmax.pointsapp.ui.screens.home

import com.reminmax.pointsapp.domain.model.Point
import com.reminmax.pointsapp.ui.base.IBaseEvent

sealed class HomeScreenEvent : IBaseEvent {
    data class NavigateToChartScreen(val points: List<Point>) : HomeScreenEvent()
}
package com.reminmax.pointsapp.ui.screens.home

import com.reminmax.pointsapp.ui.base.IBaseEvent

sealed class HomeScreenEvent : IBaseEvent {
    object NavigateToChartScreen : HomeScreenEvent()
}
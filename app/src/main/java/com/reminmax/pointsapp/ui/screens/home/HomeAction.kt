package com.reminmax.pointsapp.ui.screens.home

sealed class HomeAction {
    data class PointCountValueChanged(val value: String) : HomeAction()
    object GetPoints : HomeAction()
}
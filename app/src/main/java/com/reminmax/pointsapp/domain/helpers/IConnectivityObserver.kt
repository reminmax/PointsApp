package com.reminmax.pointsapp.domain.helpers

import kotlinx.coroutines.flow.Flow

interface IConnectivityObserver {

    fun observe(): Flow<ConnectivityStatus>

    fun isInternetConnectionAvailable(): Boolean

    enum class ConnectivityStatus {
        Available,
        Unavailable,
        Losing,
        Lost
    }
}
package com.reminmax.pointsapp.data.fake

import com.reminmax.pointsapp.domain.helpers.INetworkUtils

class FakeNetworkUtils(private val hasConnection: Boolean) : INetworkUtils {

    override fun hasNetworkConnection(): Boolean =
        hasConnection
}
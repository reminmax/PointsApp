package com.reminmax.pointsapp.data.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.reminmax.pointsapp.domain.helpers.INetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkUtils @Inject constructor(
    @ApplicationContext val context: Context,
    private val connectivityManager: ConnectivityManager,
) : INetworkUtils {

    override fun hasNetworkConnection(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}

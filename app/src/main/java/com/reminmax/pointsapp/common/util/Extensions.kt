package com.reminmax.pointsapp.common.util

import android.os.Build

fun isSdkVer29OrLater() : Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
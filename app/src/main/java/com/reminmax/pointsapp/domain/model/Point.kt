package com.reminmax.pointsapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Point(
    val x: Float,
    val y: Float
)
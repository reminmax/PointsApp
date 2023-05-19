package com.reminmax.pointsapp.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class PointDto(
    val x: Float,
    val y: Float
)
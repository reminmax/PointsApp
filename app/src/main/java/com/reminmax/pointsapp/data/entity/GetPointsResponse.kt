package com.reminmax.pointsapp.data.entity

import kotlinx.serialization.Serializable

@Serializable
class GetPointsResponse(
    val points: List<PointDto>
)
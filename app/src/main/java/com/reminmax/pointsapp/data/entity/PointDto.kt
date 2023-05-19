package com.reminmax.pointsapp.data.entity

import com.reminmax.pointsapp.domain.mapper.IConvertableTo
import com.reminmax.pointsapp.domain.model.Point
import kotlinx.serialization.Serializable

@Serializable
data class PointDto(
    val x: Float,
    val y: Float
) : IConvertableTo<Point> {
    override fun convertTo(): Point {
        return Point(
            x = x,
            y = y
        )
    }
}
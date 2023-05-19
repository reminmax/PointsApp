package com.reminmax.pointsapp.domain.repository

import com.reminmax.pointsapp.data.entity.GetPointsResponse
import com.reminmax.pointsapp.data.network.NetworkResult

interface IPointsApiDataSource {

    suspend fun getPoints(count: Int): NetworkResult<GetPointsResponse>

}
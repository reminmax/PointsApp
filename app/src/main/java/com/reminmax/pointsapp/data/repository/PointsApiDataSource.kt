package com.reminmax.pointsapp.data.repository

import com.reminmax.pointsapp.data.data_source.remote.PointsApiService
import com.reminmax.pointsapp.data.entity.GetPointsResponse
import com.reminmax.pointsapp.data.network.NetworkResult
import com.reminmax.pointsapp.domain.repository.IPointsApiDataSource
import javax.inject.Inject

class PointsApiDataSource @Inject constructor(
    private val apiService: PointsApiService
) : IPointsApiDataSource {

    override suspend fun getPoints(count: Int): NetworkResult<GetPointsResponse> =
        apiService.getPoints(count = count)
}
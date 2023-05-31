package com.reminmax.pointsapp.data.data_source.remote

import com.reminmax.pointsapp.common.util.POINTS_PATH
import com.reminmax.pointsapp.data.entity.GetPointsResponse
import com.reminmax.pointsapp.data.network.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Query

interface PointsApiService {

    @GET(POINTS_PATH)
    suspend fun getPoints(
        @Query("count") count: Int,
    ) : NetworkResult<GetPointsResponse>

}
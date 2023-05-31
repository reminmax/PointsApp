package com.reminmax.pointsapp.data.fake

import JvmUnitTestFakeAssetManager
import com.reminmax.pointsapp.data.entity.GetPointsResponse
import com.reminmax.pointsapp.data.network.NetworkResult
import com.reminmax.pointsapp.domain.repository.IPointsApiDataSource
import com.reminmax.pointsapp.util.GET_POINTS_RESPONSE_ASSET
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

class FakePointsApiDataSource : IPointsApiDataSource {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getPoints(count: Int): NetworkResult<GetPointsResponse> {
        val response =  JvmUnitTestFakeAssetManager.open(GET_POINTS_RESPONSE_ASSET).use {
            Json.decodeFromStream<GetPointsResponse>(it)
        }
        return NetworkResult.Success(response)
    }
}
package com.reminmax.pointsapp.domain.use_case

import com.reminmax.pointsapp.data.SResult
import com.reminmax.pointsapp.data.emptyResult
import com.reminmax.pointsapp.data.errorResult
import com.reminmax.pointsapp.data.loading
import com.reminmax.pointsapp.data.network.onError
import com.reminmax.pointsapp.data.network.onException
import com.reminmax.pointsapp.data.network.onSuccess
import com.reminmax.pointsapp.data.successResult
import com.reminmax.pointsapp.domain.model.Point
import com.reminmax.pointsapp.domain.repository.IPointsApiDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface IGetPointsUseCase {
    operator fun invoke(count: Int) : Flow<SResult<List<Point>>>
}

class GetPointsUseCase @Inject constructor(
    private val dataSource: IPointsApiDataSource
) : IGetPointsUseCase {

    override fun invoke(count: Int): Flow<SResult<List<Point>>> = flow {
        emit(loading())

        val response = dataSource.getPoints(count = count)
        response.onSuccess { data ->
            val body = data.points.map { pointDto ->
                pointDto.convertTo()
            }
            emit(
                if (body.isNotEmpty()) successResult(data = body) else
                    emptyResult()
            )
        }.onError { code, message ->
            emit(
                errorResult(code, message)
            )
        }.onException { ex ->
            emit(
                errorResult(0, ex.localizedMessage)
            )
        }
    }
}
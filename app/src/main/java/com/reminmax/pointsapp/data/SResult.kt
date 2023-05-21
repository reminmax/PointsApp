package com.reminmax.pointsapp.data

sealed class SResult<out T : Any> {
    class Success<out T : Any>(
        val data: T
    ) : SResult<T>()

    object Loading : SResult<Nothing>()
    object Empty : SResult<Nothing>()
    class Error(val code: Int, val message: String?) : SResult<Nothing>()
}

inline fun <reified T : Any> successResult(data: T) = SResult.Success(data)
fun loading() = SResult.Loading
fun emptyResult() = SResult.Empty
fun errorResult(code: Int, message: String?) = SResult.Error(code, message)
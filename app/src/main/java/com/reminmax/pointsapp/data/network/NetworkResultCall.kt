package com.reminmax.pointsapp.data.network

import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.Request
import retrofit2.HttpException
import timber.log.Timber

class NetworkResultCall<T : Any>(
    private val proxy: Call<T>
) : Call<NetworkResult<T>> {

    override fun enqueue(callback: Callback<NetworkResult<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {

                val networkResult = try {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        NetworkResult.Success(body)
                    } else {
                        NetworkResult.Error(
                            code = response.code(),
                            message = response.errorBody()?.string()
                        )
                    }
                } catch (ex: HttpException) {
                    Timber.e(ex)
                    NetworkResult.Error(code = ex.code(), message = ex.message())
                } catch (ex: Throwable) {
                    Timber.e(ex)
                    NetworkResult.Exception(ex)
                }

                callback.onResponse(this@NetworkResultCall, Response.success(networkResult))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val networkResult = NetworkResult.Exception<T>(t)
                callback.onResponse(this@NetworkResultCall, Response.success(networkResult))
            }
        })
    }

    override fun execute(): Response<NetworkResult<T>> {
        return Response.success(NetworkResult.Success(proxy.execute().body()!!))
    }

    override fun clone(): Call<NetworkResult<T>> = NetworkResultCall(proxy.clone())
    override fun request(): Request = proxy.request()
    override fun timeout(): Timeout = proxy.timeout()
    override fun isExecuted(): Boolean = proxy.isExecuted
    override fun isCanceled(): Boolean = proxy.isCanceled

    override fun cancel() {
        proxy.cancel()
    }
}

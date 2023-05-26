package com.reminmax.pointsapp.data.data_source.remote

import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.reminmax.pointsapp.CoroutineTestRule
import com.reminmax.pointsapp.common.util.POINTS_PATH
import com.reminmax.pointsapp.data.entity.GetPointsResponse
import com.reminmax.pointsapp.data.entity.PointDto
import com.reminmax.pointsapp.data.network.NetworkResult
import com.reminmax.pointsapp.data.network.NetworkResultCallAdapterFactory
import com.reminmax.pointsapp.data.network.onSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit

private const val POINTS_RESPONSE_FILENAME = "mock_response/GetPointsResponse.json"

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class PointsApiServiceTest {

    private lateinit var apiService: PointsApiService
    private lateinit var mockServer: MockWebServer
    private val client = OkHttpClient.Builder().build()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        mockServer = MockWebServer()

        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        apiService = Retrofit.Builder()
            .baseUrl(mockServer.url(""))
            .client(client)
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(PointsApiService::class.java)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    private fun enqueueMockResponse() {
        javaClass.classLoader?.let {
            val inputStream = it.getResourceAsStream(POINTS_RESPONSE_FILENAME)
            val source = inputStream.source().buffer()
            val mockResponse = MockResponse()
            mockResponse.apply {
                setBody(source.readString(Charsets.UTF_8))
                setResponseCode(200)
            }
            mockServer.enqueue(mockResponse)
        }
    }

    @Test
    fun `successful response`() = runTest {

        val pointCount = 20

        // Prepare fake response
        enqueueMockResponse()

        var result: List<PointDto>? = null
        val response: NetworkResult<GetPointsResponse> = apiService.getPoints(count = pointCount)
        response
            .onSuccess { data ->
                result = data.points
            }

        val request = mockServer.takeRequest()

        assertThat(result).isNotNull()
        assertThat(request.path).isEqualTo("/$POINTS_PATH?count=$pointCount")
        assertThat(result?.size).isEqualTo(pointCount)

        assertThat(result?.first()?.x).isEqualTo(-3.93f)
        assertThat(result?.first()?.y).isEqualTo(-14.00f)

    }

}
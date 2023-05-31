package com.reminmax.pointsapp.domain.use_case

import com.google.common.truth.Truth
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.reminmax.pointsapp.data.SResult
import com.reminmax.pointsapp.data.data_source.remote.PointsApiService
import com.reminmax.pointsapp.data.network.NetworkResultCallAdapterFactory
import com.reminmax.pointsapp.data.repository.PointsApiDataSource
import com.reminmax.pointsapp.util.CoroutineTestRule
import com.reminmax.pointsapp.util.GET_POINTS_RESPONSE_ASSET
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
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

private const val pointCount = 20

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class GetPointsUseCaseTest {

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private lateinit var mockWebServer: MockWebServer

    private lateinit var apiService: PointsApiService
    private lateinit var getPointsUseCase: IGetPointsUseCase

    private val client = OkHttpClient.Builder().build()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()

        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(PointsApiService::class.java)

        getPointsUseCase = GetPointsUseCase(
            dataSource = PointsApiDataSource(
                apiService = apiService
            )
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `check if 400 response results in an error state`() = runTest {
        val response = MockResponse()
            .setBody("The client messed this up")
            .setResponseCode(400)

        mockWebServer.enqueue(response)

        val flow = getPointsUseCase.invoke(count = pointCount)
        launch {
            try {
                flow.collect {}
            } catch (ex: Exception) {
                Truth.assertThat(ex.message).isEqualTo("HTTP 400 Client Error")
            }
        }
    }

    @Test
    fun `check exception for malformed JSON`() = runTest {
        val response = MockResponse()
            .setBody("Malformed JSON")
            .setResponseCode(200)

        mockWebServer.enqueue(response)

        val flow = getPointsUseCase.invoke(count = pointCount)
        launch {
            try {
                flow.collect {}
            } catch (ex: Exception) {
                Truth.assertThat(ex.message).contains("malformed JSON")
            }
        }
    }

    @Test
    fun `check successful response`() = runTest {
        val inputStream = JvmUnitTestFakeAssetManager.open(GET_POINTS_RESPONSE_ASSET)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.apply {
            setBody(source.readString(Charsets.UTF_8))
            setResponseCode(200)
        }
        mockWebServer.enqueue(mockResponse)

        val flow = getPointsUseCase.invoke(count = pointCount)
        launch {
            flow
                .filter { it !is SResult.Loading }
                .collect { result ->
                    if (result is SResult.Success) {
                        val data = result.data

                        Truth.assertThat(data).isNotNull()
                        Truth.assertThat(data.size).isEqualTo(pointCount)

                        Truth.assertThat(data[0].x).isEqualTo(-3.93f)
                        Truth.assertThat(data[0].y).isEqualTo(-14.00f)
                    }
                }
        }
    }

}
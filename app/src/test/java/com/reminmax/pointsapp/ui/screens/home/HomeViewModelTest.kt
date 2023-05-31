package com.reminmax.pointsapp.ui.screens.home

import com.google.common.truth.Truth.assertThat
import com.reminmax.pointsapp.data.fake.FakeAndroidResourceProvider
import com.reminmax.pointsapp.data.fake.FakeNetworkUtils
import com.reminmax.pointsapp.data.fake.FakePointsApiDataSource
import com.reminmax.pointsapp.domain.use_case.GetPointsUseCase
import com.reminmax.pointsapp.domain.validation.Validator
import com.reminmax.pointsapp.R
import com.reminmax.pointsapp.common.util.MAX_POINT_COUNT
import com.reminmax.pointsapp.common.util.MIN_POINT_COUNT
import com.reminmax.pointsapp.util.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class HomeViewModelTest {

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.testDispatcher

    private lateinit var viewModel: HomeViewModel

    private val pointsApiDataSource = FakePointsApiDataSource()
    private val getPointsUseCase = GetPointsUseCase(dataSource = pointsApiDataSource)
    private val resourceProvider = FakeAndroidResourceProvider()
    private val validator = Validator()

    private fun createViewModelInstance(
        hasInternetConnection: Boolean = true
    ): HomeViewModel {
        return HomeViewModel(
            getPointsUseCase = getPointsUseCase,
            resourceProvider = resourceProvider,
            validator = validator,
            networkUtils = FakeNetworkUtils(hasConnection = hasInternetConnection),
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `Go button is disabled when text field is empty`() {
        viewModel = createViewModelInstance()

        val uiState = viewModel.uiState.value

        assertThat(uiState.pointCount).isEmpty()
        assertThat(uiState.isGoButtonAvailable).isFalse()
    }

    @Test
    fun `Go button is enabled when text field is not empty`() {
        viewModel = createViewModelInstance()

        viewModel.dispatch(HomeAction.PointCountValueChanged("10"))
        val uiState = viewModel.uiState.value

        assertThat(uiState.pointCount).isNotEmpty()
        assertThat(uiState.isGoButtonAvailable).isTrue()
    }

    @Test
    fun `When trying to get points without having internet connection, an error message is displayed`() {
        viewModel = createViewModelInstance(hasInternetConnection = false)

        viewModel.dispatch(HomeAction.PointCountValueChanged("10"))
        viewModel.dispatch(HomeAction.GetPoints)

        val uiState = viewModel.uiState.value
        assertThat(uiState.errorMessage).isEqualTo(
            resourceProvider.getString(R.string.noInternetConnection)
        )
    }

    @Test
    fun `onPointCountValueChanged() test`() {
        viewModel = createViewModelInstance()

        val pointCount = "10"
        viewModel.dispatch(HomeAction.PointCountValueChanged(pointCount))

        assertThat(viewModel.uiState.value.pointCount).isEqualTo(pointCount)
    }

    @Test
    fun `Validation test`() {
        viewModel = createViewModelInstance()

        // empty value
        viewModel.dispatch(HomeAction.GetPoints)

        assertThat(viewModel.uiState.value.errorMessage).isEqualTo(
            resourceProvider.getString(R.string.valueCantBeEmptyError)
        )

        // non integer value
        viewModel.dispatch(HomeAction.PointCountValueChanged("10.5"))
        viewModel.dispatch(HomeAction.GetPoints)

        assertThat(viewModel.uiState.value.errorMessage).isEqualTo(
            resourceProvider.getString(R.string.onlyIntegerValuesAcceptedError)
        )

        // out of range value
        viewModel.dispatch(HomeAction.PointCountValueChanged("0"))
        viewModel.dispatch(HomeAction.GetPoints)

        assertThat(viewModel.uiState.value.errorMessage).isEqualTo(
            resourceProvider.getString(
                R.string.valueOutOfRangeError,
                MIN_POINT_COUNT,
                MAX_POINT_COUNT
            )
        )
    }
}
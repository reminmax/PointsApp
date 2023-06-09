package com.reminmax.pointsapp.ui.screens.chart

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.reminmax.pointsapp.data.fake.FakeAndroidResourceProvider
import com.reminmax.pointsapp.domain.model.LinearChartStyle
import com.reminmax.pointsapp.ui.navigation.NavigationParams
import com.reminmax.pointsapp.util.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class ChartViewModelTest {

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: ChartViewModel
    private val resourceProvider = FakeAndroidResourceProvider()

    private val points = "[{\"x\": -3.93,\"y\": -14.00},{\"x\": -30.48,\"y\": 63.38}]"
    private val savedStateHandle: SavedStateHandle = SavedStateHandle().apply {
        this[NavigationParams.POINTS] = points
    }

    @Before
    fun setup() {
        viewModel = ChartViewModel(
            savedStateHandle = savedStateHandle,
            resourceProvider = resourceProvider
        )
    }

    @Test
    fun `savedStateHandle test`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.points).isNotNull()
            assertThat(state.points.size).isEqualTo(2)

            val firstItem = state.points.first()
            assertThat(firstItem.x).isEqualTo(-3.93f)
            assertThat(firstItem.y).isEqualTo(-14.00f)
        }
    }

    @Test
    fun `ChartStyleSelected test`() = runTest {
        viewModel.dispatch(ChartAction.ChartStyleSelected(LinearChartStyle.SMOOTH))

        assertThat(viewModel.uiState.value.chartStyle).isEqualTo(LinearChartStyle.SMOOTH)
    }

    @Test
    fun `ShowUserMessage test`() = runTest {
        val msgText = "Some message text"
        viewModel.dispatch(ChartAction.ShowUserMessage(messageToShow = msgText))

        viewModel.userMessages.test {
            val state = awaitItem()
            assertThat(state).isNotEmpty()
            assertThat(state.size).isEqualTo(1)
            assertThat(state.first().message).isEqualTo(msgText)
        }
    }

    @Test
    fun `UserMessageShown test`() = runTest {
        val msgText = "Some message text"
        viewModel.dispatch(ChartAction.ShowUserMessage(messageToShow = msgText))

        var msgId = 0L
        viewModel.userMessages.test {
            val state = awaitItem()
            assertThat(state).isNotEmpty()
            assertThat(state.size).isEqualTo(1)
            assertThat(state.first().message).isEqualTo(msgText)
            msgId = state.first().id
        }

        viewModel.dispatch(ChartAction.UserMessageShown(messageId = msgId))
        viewModel.userMessages.test {
            val state = awaitItem()
            assertThat(state).isEmpty()
        }
    }
}
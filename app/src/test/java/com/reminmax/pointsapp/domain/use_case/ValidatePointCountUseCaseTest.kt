package com.reminmax.pointsapp.domain.use_case

import com.google.common.truth.Truth
import com.reminmax.pointsapp.data.fake.FakeAndroidResourceProvider
import com.reminmax.pointsapp.domain.validation.Validator
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.reminmax.pointsapp.R
import com.reminmax.pointsapp.common.util.MAX_POINT_COUNT
import com.reminmax.pointsapp.common.util.MIN_POINT_COUNT
import com.reminmax.pointsapp.domain.validation.IValidator

@RunWith(JUnit4::class)
class ValidatePointCountUseCaseTest {

    private lateinit var validatePointCountUseCase: IValidatePointCountUseCase
    private val resourceProvider = FakeAndroidResourceProvider()
    private lateinit var validator: IValidator

    @Before
    fun setup() {
        validator = Validator()
        validatePointCountUseCase = ValidatePointCountUseCase(
            validator = validator,
            resourceProvider = resourceProvider
        )
    }

    @Test
    fun `successful result for correct target value`() {
        val targetValue = "10"

        val result = validatePointCountUseCase.invoke(targetValue)

        Truth.assertThat(result.successful).isTrue()
        Truth.assertThat(result.errorMessage).isNull()
    }

    @Test
    fun `failed result for an empty target value`() {
        val targetValue = ""

        val result = validatePointCountUseCase.invoke(targetValue)

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorMessage).isEqualTo(
            resourceProvider.getString(R.string.valueCantBeEmptyError)
        )
    }

    @Test
    fun `failed result for not integer value`() {
        val targetValue = "10.0"

        val result = validatePointCountUseCase.invoke(targetValue)

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorMessage).isEqualTo(
            resourceProvider.getString(R.string.onlyIntegerValuesAcceptedError)
        )
    }

    @Test
    fun `failed result for incorrect target value`() {
        val targetValue = "10d"

        val result = validatePointCountUseCase.invoke(targetValue)

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorMessage).isEqualTo(
            resourceProvider.getString(R.string.shouldNotContainAnyAlphabetCharactersError)
        )
    }

    @Test
    fun `failed result if value isn't in range`() {
        val targetValue = "101"
        val errorMsg = resourceProvider.getString(
            R.string.valueOutOfRangeError,
            MIN_POINT_COUNT,
            MAX_POINT_COUNT
        )

        val result = validatePointCountUseCase.invoke(targetValue)

        Truth.assertThat(result.successful).isFalse()
        Truth.assertThat(result.errorMessage).isEqualTo(errorMsg)
    }
}
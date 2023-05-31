package com.reminmax.pointsapp.domain.validation

import com.google.common.truth.Truth.assertThat
import com.reminmax.pointsapp.domain.validation.rules.IsInRangeValidationRule
import com.reminmax.pointsapp.domain.validation.rules.IsIntegerValidationRule
import com.reminmax.pointsapp.domain.validation.rules.NotEmptyValidationRule
import com.reminmax.pointsapp.domain.validation.rules.OnlyNumbersValidationRule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ValidatorTest {

    private lateinit var validator: IValidator

    @Before
    fun setup() {
        validator = Validator()
    }

    // region NotEmptyValidationRule
    @Test
    fun `notEmptyValidationRule correct string returns true`() {
        var result = true
        val targetValue = "10"

        validator
            .addValidationRules(
                NotEmptyValidationRule("")
            )
            .addErrorCallback {
                result = false
            }
            .validate(targetValue)

        assertThat(result).isTrue()
    }

    @Test
    fun `notEmptyValidationRule incorrect string returns false`() {
        var result = true
        val targetValue = ""

        validator
            .addValidationRules(
                NotEmptyValidationRule("")
            )
            .addErrorCallback {
                result = false
            }
            .validate(targetValue)

        assertThat(result).isFalse()
    }
    // endregion

    // region OnlyNumbersValidationRule
    @Test
    fun `OnlyNumbersValidationRule correct string returns true`() {
        var result = true
        val targetValue = "10"

        validator
            .addValidationRules(
                OnlyNumbersValidationRule("")
            )
            .addErrorCallback {
                result = false
            }
            .validate(targetValue)

        assertThat(result).isTrue()
    }

    @Test
    fun `OnlyNumbersValidationRule incorrect string returns false`() {
        var result = true
        val targetValue = "1d"

        validator
            .addValidationRules(
                OnlyNumbersValidationRule("")
            )
            .addErrorCallback {
                result = false
            }
            .validate(targetValue)

        assertThat(result).isFalse()
    }
    // endregion

    // region IsIntegerValidationRule
    @Test
    fun `IsIntegerValidationRule correct string returns true`() {
        var result = true
        val targetValue = "10"

        validator
            .addValidationRules(
                IsIntegerValidationRule("")
            )
            .addErrorCallback {
                result = false
            }
            .validate(targetValue)

        assertThat(result).isTrue()
    }

    @Test
    fun `IsIntegerValidationRule incorrect string returns false`() {
        var result = true
        val targetValue = "10.0"

        validator
            .addValidationRules(
                IsIntegerValidationRule("")
            )
            .addErrorCallback {
                result = false
            }
            .validate(targetValue)

        assertThat(result).isFalse()
    }
    // endregion

    // region IsInRangeValidationRule
    @Test
    fun `IsInRangeValidationRule correct value returns true`() {
        var result = true
        val targetValue = "10"
        val startRange = 1
        val endRange = 11

        validator
            .addValidationRules(
                IsInRangeValidationRule(
                    startRange = startRange,
                    endRange = endRange,
                    errorMsg = ""
                )
            )
            .addErrorCallback {
                result = false
            }
            .validate(targetValue)

        assertThat(result).isTrue()
    }

    @Test
    fun `IsInRangeValidationRule incorrect value returns false`() {
        var result = true
        val targetValue = "15"
        val startRange = 1
        val endRange = 11

        validator
            .addValidationRules(
                IsInRangeValidationRule(
                    startRange = startRange,
                    endRange = endRange,
                    errorMsg = ""
                )
            )
            .addErrorCallback {
                result = false
            }
            .validate(targetValue)

        assertThat(result).isFalse()
    }
    // endregion
}
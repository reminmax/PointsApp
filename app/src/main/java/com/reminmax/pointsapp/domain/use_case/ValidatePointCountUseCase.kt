package com.reminmax.pointsapp.domain.use_case

import com.reminmax.pointsapp.R
import com.reminmax.pointsapp.common.util.MAX_POINT_COUNT
import com.reminmax.pointsapp.common.util.MIN_POINT_COUNT
import com.reminmax.pointsapp.domain.resource_provider.IResourceProvider
import com.reminmax.pointsapp.domain.validation.IValidator
import com.reminmax.pointsapp.domain.validation.ValidationResult
import com.reminmax.pointsapp.domain.validation.rules.IsInRangeValidationRule
import com.reminmax.pointsapp.domain.validation.rules.IsIntegerValidationRule
import com.reminmax.pointsapp.domain.validation.rules.NotEmptyValidationRule
import com.reminmax.pointsapp.domain.validation.rules.OnlyNumbersValidationRule
import javax.inject.Inject

interface IValidatePointCountUseCase {
    operator fun invoke(value: String): ValidationResult
}

class ValidatePointCountUseCase @Inject constructor(
    private val validator: IValidator,
    private val resourceProvider: IResourceProvider,
) : IValidatePointCountUseCase {

    override fun invoke(value: String): ValidationResult {
        var successful = false
        var errorMessage: String? = null

        validator
            .addValidationRules(
                NotEmptyValidationRule(
                    errorMsg = resourceProvider.getString(R.string.valueCantBeEmptyError)
                ),
                IsIntegerValidationRule(
                    errorMsg = resourceProvider.getString(R.string.onlyIntegerValuesAcceptedError)
                ),
                OnlyNumbersValidationRule(
                    errorMsg = resourceProvider.getString(
                        R.string.shouldNotContainAnyAlphabetCharactersError
                    )
                ),
                IsInRangeValidationRule(
                    startRange = MIN_POINT_COUNT,
                    endRange = MAX_POINT_COUNT,
                    errorMsg = resourceProvider.getString(
                        R.string.valueOutOfRangeError,
                        MIN_POINT_COUNT,
                        MAX_POINT_COUNT
                    )
                )
            )
            .addSuccessCallback {
                successful = true
            }
            .addErrorCallback { errorMsg ->
                successful = false
                errorMessage = errorMsg
            }
            .validate(value)

        return ValidationResult(
            successful = successful,
            errorMessage = errorMessage
        )
    }
}
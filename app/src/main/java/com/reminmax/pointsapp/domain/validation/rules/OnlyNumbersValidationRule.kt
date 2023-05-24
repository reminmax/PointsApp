package com.reminmax.pointsapp.domain.validation.rules

private const val REGEX_PATTERN = "\\d+"

class OnlyNumbersValidationRule(
    var errorMsg: String
) : BaseValidationRule{

    override fun validate(value: String): Boolean = value.matches(Regex(REGEX_PATTERN))

    override fun getErrorMessage(): String = errorMsg

    override fun setError(message: String) {
        errorMsg = message
    }
}
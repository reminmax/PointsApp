package com.reminmax.pointsapp.domain.validation.rules

class IsIntegerValidationRule(
    var errorMsg: String
) : BaseValidationRule {

    override fun validate(value: String): Boolean =
        value.toIntOrNull() != null

    override fun getErrorMessage(): String = errorMsg

    override fun setError(message: String) {
        errorMsg = message
    }
}
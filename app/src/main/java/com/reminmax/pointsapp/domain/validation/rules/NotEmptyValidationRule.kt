package com.reminmax.pointsapp.domain.validation.rules

class NotEmptyValidationRule(
    var errorMsg: String
) : BaseValidationRule {

    override fun validate(value: String): Boolean = value.isNotEmpty()

    override fun getErrorMessage(): String = errorMsg

    override fun setError(message: String) {
        errorMsg = message
    }
}
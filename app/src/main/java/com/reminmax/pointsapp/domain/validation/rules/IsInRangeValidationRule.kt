package com.reminmax.pointsapp.domain.validation.rules

import java.lang.NumberFormatException

class IsInRangeValidationRule(
    private val startRange: Int,
    private val endRange: Int,
    var errorMsg: String
) : BaseValidationRule {

    override fun validate(value: String): Boolean {
        return try {
            value.toInt() in startRange..endRange
        } catch (ex: NumberFormatException) {
            false
        }
    }

    override fun getErrorMessage(): String = errorMsg

    override fun setError(message: String) {
        errorMsg = message
    }
}
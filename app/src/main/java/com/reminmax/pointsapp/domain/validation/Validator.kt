package com.reminmax.pointsapp.domain.validation

import javax.inject.Inject

private const val REGEX_PATTERN = "\\d+"

class Validator @Inject constructor() : IValidator {

    override fun validate(value: String): Boolean {
        val positiveInteger = value.matches(Regex(REGEX_PATTERN))
        if (!positiveInteger) return false

        return value.toInt() in 1..100
    }
}
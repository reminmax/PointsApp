package com.reminmax.pointsapp.domain.validation.rules

interface BaseValidationRule {

    fun validate(value: String) : Boolean
    fun getErrorMessage() : String
    fun setError(message: String)

}
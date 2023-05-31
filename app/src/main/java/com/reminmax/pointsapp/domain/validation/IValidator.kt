package com.reminmax.pointsapp.domain.validation

import com.reminmax.pointsapp.domain.validation.rules.BaseValidationRule

interface IValidator {
    fun addValidationRules(vararg rules: BaseValidationRule): IValidator
    fun addErrorCallback(callback: (message: String) -> Unit): IValidator
    fun addSuccessCallback(callback: () -> Unit): IValidator
    fun validate(value: String): Boolean
}
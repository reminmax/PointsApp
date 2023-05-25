package com.reminmax.pointsapp.domain.validation

import com.reminmax.pointsapp.domain.validation.rules.BaseValidationRule
import javax.inject.Inject

class Validator @Inject constructor() : IValidator {

    private var rulesValidationList = ArrayList<BaseValidationRule>()

    private var isValid = true
    private var errorMessageText: String = ""

    var errorCallback: ((message: String) -> Unit)? = null
    var successCallback: (() -> Unit)? = null

    override fun addValidationRules(vararg rules: BaseValidationRule): IValidator {
        rulesValidationList.clear()
        rules.forEach { validationRule ->
            rulesValidationList.add(validationRule)
        }
        return this
    }

    override fun addErrorCallback(callback: (message: String) -> Unit): IValidator {
        errorCallback = callback
        return this
    }

    override fun addSuccessCallback(callback: () -> Unit): IValidator {
        successCallback = callback
        return this
    }

    override fun validate(value: String): Boolean {
        for (rule in rulesValidationList) {
            if (!rule.validate(value)) {
                isValid = false
                setErrorMessageText(rule.getErrorMessage())
                break
            }
        }

        if (isValid)
            successCallback?.invoke()
        else
            errorCallback?.invoke(errorMessageText)

        return isValid
    }

    private fun setErrorMessageText(errorMessage: String) {
        isValid = false
        errorMessageText = errorMessage
    }

}
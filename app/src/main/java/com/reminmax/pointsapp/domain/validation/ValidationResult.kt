package com.reminmax.pointsapp.domain.validation

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
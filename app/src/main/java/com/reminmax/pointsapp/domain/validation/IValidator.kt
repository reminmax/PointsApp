package com.reminmax.pointsapp.domain.validation

interface IValidator {
    fun validate(value: String): Boolean
}
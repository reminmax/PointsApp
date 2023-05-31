package com.reminmax.pointsapp.domain.mapper

interface IConvertableTo<T> {
    fun convertTo(): T?
}
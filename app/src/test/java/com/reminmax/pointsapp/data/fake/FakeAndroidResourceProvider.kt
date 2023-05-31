package com.reminmax.pointsapp.data.fake

import com.reminmax.pointsapp.R
import com.reminmax.pointsapp.domain.resource_provider.IResourceProvider

class FakeAndroidResourceProvider : IResourceProvider {
    override fun getString(resourceId: Int): String =
        when (resourceId) {
            R.string.anUnexpectedErrorOccurred ->
                "Произошла неизвестная ошибка"
            R.string.homeScreenInfo ->
                "Введите количество точек и нажмите кнопку Поехали"
            R.string.enterPointCount ->
                "Введите количество точек"
            R.string.clearTextField ->
                "Очистить поле ввода"
            R.string.pointCountPlaceholder ->
                "10"
            R.string.goButtonLabel ->
                "Поехали!"
            R.string.goBack ->
                "Назад"
            R.string.chartScreenHeader ->
                "График"
            R.string.ok ->
                "ОК"
            R.string.saveToFile ->
                "Сохранить в файл"
            R.string.emptyResultErrorText ->
                "Запрос выполнен успешно, но получено пустое значение"
            R.string.valueCantBeEmptyError ->
                "Значение не может быть пустым"
            R.string.shouldNotContainAnyAlphabetCharactersError ->
                "Допускается ввод только числовых символов"
            R.string.onlyIntegerValuesAcceptedError ->
                "Допускается ввод только целых чисел"
            R.string.noInternetConnection ->
                "Отсутствует интернет соединение"
            else -> ""
        }

    override fun getString(resourceId: Int, vararg args: Any): String =
        when (resourceId) {
            R.string.valueOutOfRangeError ->
                "Допускается ввод только целых чисел от ${args[0]} до $${args[1]}"
            else -> ""
        }
}
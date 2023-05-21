package com.reminmax.pointsapp.di

import com.reminmax.pointsapp.domain.validation.IValidator
import com.reminmax.pointsapp.domain.validation.Validator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ValidatorModule {

    @Binds
    abstract fun bindValidator(implementation: Validator): IValidator
}
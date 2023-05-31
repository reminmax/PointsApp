package com.reminmax.pointsapp.di

import com.reminmax.pointsapp.domain.use_case.GetPointsUseCase
import com.reminmax.pointsapp.domain.use_case.IGetPointsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetPointsUseCase(
        implementation: GetPointsUseCase
    ): IGetPointsUseCase
}
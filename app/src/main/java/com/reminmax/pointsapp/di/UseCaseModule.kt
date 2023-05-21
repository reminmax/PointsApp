package com.reminmax.pointsapp.di

import com.reminmax.pointsapp.domain.use_case.GetPointsUseCase
import com.reminmax.pointsapp.domain.use_case.IGetPointsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindGetPointsUseCase(
        implementation: GetPointsUseCase
    ): IGetPointsUseCase

}
package com.reminmax.pointsapp.di

import com.reminmax.pointsapp.data.repository.PointsApiDataSource
import com.reminmax.pointsapp.domain.repository.IPointsApiDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindPointsApiDataSource(impl: PointsApiDataSource): IPointsApiDataSource

}
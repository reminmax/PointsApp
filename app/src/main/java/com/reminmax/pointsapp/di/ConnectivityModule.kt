package com.reminmax.pointsapp.di

import com.reminmax.pointsapp.common.helpers.NetworkConnectivityObserver
import com.reminmax.pointsapp.domain.helpers.IConnectivityObserver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ConnectivityModule {

    @Binds
    abstract fun bindConnectivityObserver(implementation: NetworkConnectivityObserver): IConnectivityObserver

}
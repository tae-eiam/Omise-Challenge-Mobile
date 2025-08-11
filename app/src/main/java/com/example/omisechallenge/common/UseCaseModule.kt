package com.example.omisechallenge.common

import com.example.omisechallenge.domain.usecase.StoreUseCase
import com.example.omisechallenge.domain.usecase.StoreUseCaseImpl
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
    abstract fun bindUseCase(
        storeUseCaseImpl: StoreUseCaseImpl
    ): StoreUseCase
}
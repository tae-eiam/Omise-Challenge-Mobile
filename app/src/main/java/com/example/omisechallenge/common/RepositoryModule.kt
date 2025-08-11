package com.example.omisechallenge.common

import com.example.omisechallenge.data.repository.StoreRepository
import com.example.omisechallenge.data.repository.StoreRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(
        storeRepositoryImpl: StoreRepositoryImpl
    ): StoreRepository
}
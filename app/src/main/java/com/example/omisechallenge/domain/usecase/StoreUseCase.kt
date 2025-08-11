package com.example.omisechallenge.domain.usecase

import com.example.omisechallenge.domain.model.Store

interface StoreUseCase {
    suspend fun getStoreInfo(): Store
}
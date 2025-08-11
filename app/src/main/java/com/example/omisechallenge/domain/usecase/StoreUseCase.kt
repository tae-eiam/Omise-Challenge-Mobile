package com.example.omisechallenge.domain.usecase

import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.Store

interface StoreUseCase {
    suspend fun getStoreInfo(): Store
    suspend fun getProducts(): List<Product>
}
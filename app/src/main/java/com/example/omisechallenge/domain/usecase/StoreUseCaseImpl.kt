package com.example.omisechallenge.domain.usecase

import com.example.omisechallenge.data.repository.StoreRepository
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.Store
import javax.inject.Inject

class StoreUseCaseImpl @Inject constructor(
    private val storeRepository: StoreRepository
): StoreUseCase {

    override suspend fun getStoreInfo(): Store {
        return storeRepository.getStoreInfo()
    }

    override suspend fun getProducts(): List<Product> {
        return storeRepository.getProducts()
    }
}
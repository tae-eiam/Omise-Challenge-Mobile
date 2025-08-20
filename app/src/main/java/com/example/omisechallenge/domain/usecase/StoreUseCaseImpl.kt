package com.example.omisechallenge.domain.usecase

import com.example.omisechallenge.data.ApiResult
import com.example.omisechallenge.data.model.request.OrderRequest
import com.example.omisechallenge.data.repository.StoreRepository
import com.example.omisechallenge.domain.model.ProductResult
import com.example.omisechallenge.domain.model.Store
import javax.inject.Inject

class StoreUseCaseImpl @Inject constructor(
    private val storeRepository: StoreRepository
): StoreUseCase {

    override suspend fun getStoreInfo(): ApiResult<Store> {
        return storeRepository.getStoreInfo()
    }

    override suspend fun getProducts(): ApiResult<ProductResult> {
        return storeRepository.getProducts()
    }

    override suspend fun makeOrder(orderRequest: OrderRequest): ApiResult<Unit> {
        return storeRepository.makeOrder(orderRequest)
    }
}
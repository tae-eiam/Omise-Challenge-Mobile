package com.example.omisechallenge.domain.usecase

import com.example.omisechallenge.data.ApiResult
import com.example.omisechallenge.data.model.request.OrderRequest
import com.example.omisechallenge.domain.model.ProductResult
import com.example.omisechallenge.domain.model.Store

interface StoreUseCase {
    suspend fun getStoreInfo(): ApiResult<Store>
    suspend fun getProducts(): ApiResult<ProductResult>
    suspend fun makeOrder(orderRequest: OrderRequest): ApiResult<Unit>
}
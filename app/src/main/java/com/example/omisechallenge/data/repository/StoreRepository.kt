package com.example.omisechallenge.data.repository

import com.example.omisechallenge.data.ApiResult
import com.example.omisechallenge.data.model.request.OrderRequest
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.Store

interface StoreRepository {
    suspend fun getStoreInfo(): ApiResult<Store>
    suspend fun getProducts(): ApiResult<List<Product>>
    suspend fun makeOrder(orderRequest: OrderRequest): ApiResult<Unit>
}
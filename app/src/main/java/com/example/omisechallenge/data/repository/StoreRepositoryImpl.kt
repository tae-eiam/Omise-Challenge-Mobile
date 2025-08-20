package com.example.omisechallenge.data.repository

import com.example.omisechallenge.data.ApiResult
import com.example.omisechallenge.data.model.request.OrderRequest
import com.example.omisechallenge.data.service.ApiService
import com.example.omisechallenge.domain.model.ProductResult
import com.example.omisechallenge.domain.model.Store
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): StoreRepository {

    override suspend fun getStoreInfo(): ApiResult<Store> {
        return try {
            val response = apiService.getStoreInfo()
            if (response.isSuccessful) {
                ApiResult.Success(response.body()!!.toStore())
            } else {
                ApiResult.Error(response.code(), response.errorBody()?.string() ?: "Unknown error")
            }
        } catch (e: Exception) {
            ApiResult.Error(-1, e.message ?: "Unknown error")
        }
    }

    override suspend fun getProducts(): ApiResult<ProductResult> {
        return try {
            val response = apiService.getProducts()
            if (response.isSuccessful) {
                ApiResult.Success(response.body()!!.data!!.productResultResponse!!.toProductResult())
            } else {
                ApiResult.Error(response.code(), response.errorBody()?.string() ?: "Unknown error")
            }
        } catch (e: Exception) {
            ApiResult.Error(-1, e.message ?: "Unknown error")
        }
    }

    override suspend fun makeOrder(orderRequest: OrderRequest): ApiResult<Unit> {
        return try {
            val response = apiService.makeOrder(orderRequest)
            if (response.isSuccessful) {
                ApiResult.Success(Unit)
            } else {
                ApiResult.Error(response.code(), response.errorBody()?.string() ?: "Unknown error")
            }
        } catch (e: Exception) {
            ApiResult.Error(-1, e.message ?: "Unknown error")
        }
    }
}
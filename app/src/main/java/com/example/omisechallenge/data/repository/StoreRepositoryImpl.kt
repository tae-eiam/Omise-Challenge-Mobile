package com.example.omisechallenge.data.repository

import com.example.omisechallenge.data.service.ApiService
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.Store
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): StoreRepository {

    override suspend fun getStoreInfo(): Store {
        return apiService.getStoreInfo().toStore()
    }

    override suspend fun getProducts(): List<Product> {
        val productResponseList = apiService.getProducts()
        val productList = mutableListOf<Product>()

        productResponseList.forEach { productResponse ->
            productList.add(productResponse.toProduct())
        }

        return productList
    }
}
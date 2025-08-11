package com.example.omisechallenge.data.repository

import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.Store

interface StoreRepository {
    suspend fun getStoreInfo(): Store
    suspend fun getProducts(): List<Product>
}
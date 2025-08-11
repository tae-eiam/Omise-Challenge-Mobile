package com.example.omisechallenge.data.repository

import com.example.omisechallenge.data.service.ApiService
import com.example.omisechallenge.domain.model.Store
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): StoreRepository {

    override suspend fun getStoreInfo(): Store {
        return apiService.getStoreInfo().toStore()
    }
}
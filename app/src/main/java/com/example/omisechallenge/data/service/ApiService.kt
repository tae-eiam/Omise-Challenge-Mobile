package com.example.omisechallenge.data.service

import com.example.omisechallenge.data.model.response.StoreResponse
import retrofit2.http.GET

interface ApiService {
    @GET("/storeInfo")
    suspend fun getStoreInfo(): StoreResponse
}
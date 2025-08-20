package com.example.omisechallenge.data.service

import com.example.omisechallenge.data.model.request.OrderRequest
import com.example.omisechallenge.data.model.response.ProductApiResponse
import com.example.omisechallenge.data.model.response.StoreResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/storeInfo")
    suspend fun getStoreInfo(): Response<StoreResponse>

    @GET("/products")
    suspend fun getProducts(): Response<ProductApiResponse>

    @POST("/order")
    suspend fun makeOrder(@Body orderRequest: OrderRequest): Response<ResponseBody>
}
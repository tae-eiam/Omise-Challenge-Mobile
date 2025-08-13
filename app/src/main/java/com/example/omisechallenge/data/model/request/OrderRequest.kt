package com.example.omisechallenge.data.model.request

import com.example.omisechallenge.domain.model.Product

data class OrderRequest(
    val products: List<Product>,
    val delivery_address: String
)

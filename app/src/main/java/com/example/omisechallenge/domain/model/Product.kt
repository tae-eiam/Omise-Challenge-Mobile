package com.example.omisechallenge.domain.model

data class ProductResult(
    val paginationInfo: PaginationInfo,
    val productList: List<Product>
)

data class PaginationInfo(
    val totalCount: Int,
    val currentPage: Int,
    val totalPages: Int,
)

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val imageUrl: String
)

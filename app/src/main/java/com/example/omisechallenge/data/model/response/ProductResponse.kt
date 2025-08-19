package com.example.omisechallenge.data.model.response

import com.example.omisechallenge.domain.model.PaginationInfo
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.ProductResult
import com.google.gson.annotations.SerializedName


data class ProductApiResponse(
    @SerializedName("data") val data: ProductDataResponse?
)

data class ProductDataResponse(
    @SerializedName("ProductResult") val productResultResponse: ProductResultResponse?
)

data class ProductResultResponse(
    @SerializedName("PaginationInfo") val paginationInfoResponse: PaginationInfoResponse?,
    @SerializedName("Products") val productListResponse: List<ProductResponse>?
) {
    fun toProductResult(): ProductResult {
        return ProductResult(
            paginationInfo = this.paginationInfoResponse?.toPaginationInfo() ?: PaginationInfo(0, 0, 0),
            productList = this.productListResponse?.map { productResponse -> productResponse.toProduct() } ?: listOf()
        )
    }
}

data class PaginationInfoResponse(
    @SerializedName("total_count") val totalCount: Int?,
    @SerializedName("current_page") val currentPage: Int?,
    @SerializedName("total_pages") val totalPages: Int?,
) {
    fun toPaginationInfo() : PaginationInfo {
        return PaginationInfo(
            totalCount = this.totalCount ?: 0,
            currentPage = this.currentPage ?: 0,
            totalPages = this.totalPages ?: 0
        )
    }
}

data class ProductResponse(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("price") val price: Int?,
    @SerializedName("imageUrl") val imageUrl: String?
) {
    fun toProduct(): Product {
        return Product(
            id = this.id ?: 0,
            name = this.name ?: "",
            price = this.price ?: 0,
            imageUrl = this.imageUrl ?: ""
        )
    }
}


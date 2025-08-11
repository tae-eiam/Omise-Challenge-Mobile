package com.example.omisechallenge.data.model.response

import com.example.omisechallenge.domain.model.Product

data class ProductResponse(
    val name: String?,
    val price: Int?,
    val imageUrl: String?
) {
    fun toProduct(): Product {
        return Product(
            name = this.name ?: "",
            price = this.price ?: 0,
            imageUrl = this.imageUrl ?: ""
        )
    }
}
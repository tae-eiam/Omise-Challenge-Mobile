package com.example.omisechallenge.data.model.response

import com.example.omisechallenge.domain.model.Store

data class StoreResponse(
    val name: String?,
    val rating: Double?,
    val openingTime: String?,
    val closingTime: String?
) {
    fun toStore(): Store {
        return Store(
            name = this.name ?: "",
            rating = this.rating ?: 0.0,
            openingTime = this.openingTime ?: "",
            closingTime = this.closingTime ?: ""
        )
    }
}
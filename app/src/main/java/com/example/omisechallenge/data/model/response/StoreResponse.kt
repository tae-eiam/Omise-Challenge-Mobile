package com.example.omisechallenge.data.model.response

import com.example.omisechallenge.domain.model.Store
import com.google.gson.annotations.SerializedName

data class StoreResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("openingTime") val openingTime: String?,
    @SerializedName("closingTime") val closingTime: String?
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
package com.example.omisechallenge.data.model.request

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("delivery_address") val address: String
)
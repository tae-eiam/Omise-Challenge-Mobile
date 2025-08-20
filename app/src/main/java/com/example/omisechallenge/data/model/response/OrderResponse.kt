package com.example.omisechallenge.data.model.response

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("status") val status: List<StatusResponse>?
)

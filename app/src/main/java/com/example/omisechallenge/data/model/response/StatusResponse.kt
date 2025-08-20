package com.example.omisechallenge.data.model.response

import com.google.gson.annotations.SerializedName

data class StatusResponse(
    @SerializedName("code") val code: String?,
    @SerializedName("message") val message: String?
)

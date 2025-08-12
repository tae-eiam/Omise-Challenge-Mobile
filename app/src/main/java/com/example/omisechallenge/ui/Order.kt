package com.example.omisechallenge.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    var name: String = "",
    var price: Int = 0,
    var imageUrl: String = "",
    var amount: Int = 0
): Parcelable

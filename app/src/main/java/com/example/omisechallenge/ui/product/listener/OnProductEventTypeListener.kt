package com.example.omisechallenge.ui.product.listener

import com.example.omisechallenge.domain.model.Product

fun interface OnProductEventTypeListener {
    fun onEventCallback(onEventTypeListener: OnEventTypeListener)
}

sealed interface OnEventTypeListener {
    data class OnAddOrderClickListener(val product: Product, val amount: Int): OnEventTypeListener
    data class OnRemoveOrderClickListener(val product: Product, val amount: Int): OnEventTypeListener
}

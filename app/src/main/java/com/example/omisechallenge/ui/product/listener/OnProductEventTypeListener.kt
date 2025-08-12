package com.example.omisechallenge.ui.product.listener

import com.example.omisechallenge.ui.model.Order

fun interface OnProductEventTypeListener {
    fun onEventCallback(onEventTypeListener: OnEventTypeListener)
}

sealed interface OnEventTypeListener {
    data class OnAddOrderClickListener(val order: Order): OnEventTypeListener
    data class OnRemoveOrderClickListener(val order: Order): OnEventTypeListener
}

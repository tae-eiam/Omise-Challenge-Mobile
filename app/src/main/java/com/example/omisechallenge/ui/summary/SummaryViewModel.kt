package com.example.omisechallenge.ui.summary

import androidx.lifecycle.ViewModel
import com.example.omisechallenge.ui.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(): ViewModel() {
    lateinit var orderList: List<Order>
    var address: String = ""

    fun getTotalPayment(): Int {
        var total = 0

        orderList.forEach { order ->
            total += order.price * order.amount
        }

        return total
    }
}
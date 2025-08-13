package com.example.omisechallenge.ui.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omisechallenge.data.ApiResult
import com.example.omisechallenge.data.model.request.OrderRequest
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.usecase.StoreUseCase
import com.example.omisechallenge.ui.UiState
import com.example.omisechallenge.ui.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(): ViewModel() {
    @Inject
    lateinit var storeUseCase: StoreUseCase

    private var _paymentState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val paymentState: StateFlow<UiState<Unit>> = _paymentState

    lateinit var orderList: List<Order>
    var address: String = ""

    fun getTotalPayment(): Int {
        var total = 0

        orderList.forEach { order ->
            total += order.price * order.amount
        }

        return total
    }

    fun validatePayment() {
        val orderRequest = OrderRequest(
            products = getProductListFromOrderList(),
            delivery_address = address
        )

        viewModelScope.launch {
            _paymentState.value = UiState.Loading
            val result = storeUseCase.makeOrder(orderRequest)
            when(result) {
                is ApiResult.Success -> _paymentState.value = UiState.Success(result.data)
                is ApiResult.Error -> _paymentState.value = UiState.Error(result.message)
            }
        }
    }

    private fun getProductListFromOrderList(): List<Product> {
        val productList = mutableListOf<Product>()

        orderList.forEach { order ->
            productList.add(Product(
                name = order.name,
                price = order.price,
                imageUrl = order.imageUrl
            ))
        }

        return productList
    }
}
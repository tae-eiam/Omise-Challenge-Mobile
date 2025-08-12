package com.example.omisechallenge.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.Store
import com.example.omisechallenge.domain.usecase.StoreUseCase
import com.example.omisechallenge.ui.model.Order
import com.example.omisechallenge.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
) : ViewModel() {
    private var _storeState = MutableStateFlow<UiState<Store>>(UiState.Loading)
    val storeState: StateFlow<UiState<Store>> = _storeState

    private var _productState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val productState: StateFlow<UiState<List<Product>>> = _productState

    private var _selectedOrderList: MutableList<Order> = mutableListOf()
    val selectedOrderList: List<Order> = _selectedOrderList

    fun loadStoreInfo() {
        viewModelScope.launch {
            _storeState.value = UiState.Loading
            try {
                val storeInfo = storeUseCase.getStoreInfo()
                _storeState.value = UiState.Success(storeInfo)
            } catch (e: Exception) {
                _storeState.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun loadProducts() {
        viewModelScope.launch {
            _productState.value = UiState.Loading
            try {
                val products = storeUseCase.getProducts()
                _productState.value = UiState.Success(products)
            } catch (e: Exception) {
                _productState.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun convertTimeFormat(time: String): String {
        val today = LocalDate.now(ZoneOffset.UTC)
        val fullTimeString = "${today}T$time"

        val instant = Instant.parse(fullTimeString)
        val zonedDateTime = instant.atZone(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return zonedDateTime.format(formatter)
    }

    fun isStoreOpen(openingTimeString: String, closingTimeString: String): Boolean {
        val openingTime = OffsetTime.parse(openingTimeString)
        val closingTime = OffsetTime.parse(closingTimeString)
        val currentTime = OffsetTime.now(ZoneId.systemDefault())

        if (openingTime.isBefore(closingTime)) {
            return currentTime.isAfter(openingTime) && currentTime.isBefore(closingTime)
        }
        return currentTime.isAfter(openingTime) || currentTime.isBefore(closingTime)
    }

    fun convertProductListToOrderList(productList: List<Product>): List<Order> {
        val orderList = mutableListOf<Order>()

        productList.forEach { product ->
            orderList.add(Order().apply {
                name = product.name
                price = product.price
                imageUrl = product.imageUrl
            })
        }

        for (selectedOrder in selectedOrderList) {
            for (order in orderList) {
                if (selectedOrder.name == order.name) {
                    order.amount = selectedOrder.amount
                    break
                }
            }
        }

        return orderList
    }

    fun updateOrder(order: Order) {
        // Check if there is in the list
        for (selectedOrder in _selectedOrderList) {
            if (selectedOrder.name == order.name) {
                if (order.amount == 0) {
                    _selectedOrderList.remove(selectedOrder)
                } else {
                    selectedOrder.amount = order.amount
                }
                return
            }
        }

        // Add new order to the list
        _selectedOrderList.add(Order(
            name = order.name,
            price = order.price,
            imageUrl = order.imageUrl,
            amount = order.amount
        ))

        // Sort by name when new order added
        _selectedOrderList.sortBy { it.name }
    }
}
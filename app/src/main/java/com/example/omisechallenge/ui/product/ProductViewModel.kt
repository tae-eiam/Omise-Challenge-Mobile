package com.example.omisechallenge.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omisechallenge.data.ApiResult
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
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
) : ViewModel() {
    private var _storeState = MutableStateFlow<UiState<Store>>(UiState.Idle)
    val storeState: StateFlow<UiState<Store>> = _storeState

    private var _productState = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val productState: StateFlow<UiState<List<Product>>> = _productState

    private var _selectedOrderList: MutableList<Order> = mutableListOf()
    val selectedOrderList: List<Order> = _selectedOrderList

    var isLastPage = false
    private var currentPage = 1
    private var productList = mutableListOf<Product>()

    fun loadStoreInfo() {
        if (_storeState.value is UiState.Idle) {
            viewModelScope.launch {
                _storeState.value = UiState.Loading
                val storeInfo = storeUseCase.getStoreInfo()
                when(storeInfo) {
                    is ApiResult.Success -> {
                        _storeState.value = UiState.Success(storeInfo.data)
                        loadProducts()
                    }
                    is ApiResult.Error -> _storeState.value = UiState.Error(storeInfo.message)
                }
            }
        }
    }

    fun loadProducts() {
        if (_productState.value is UiState.Loading || isLastPage) {
            return
        }

        viewModelScope.launch {
            _productState.value = UiState.Loading
            val products = storeUseCase.getProducts(currentPage)
            when(products) {
                is ApiResult.Success -> {
                    if (currentPage >= products.data.paginationInfo.totalPages) {
                        isLastPage = true
                    }

                    productList.addAll(products.data.productList)
                    _productState.value = UiState.Success(productList)
                    currentPage++
                }
                is ApiResult.Error -> {
                    _productState.value = UiState.Error(products.message)
                }
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
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(ZoneOffset.UTC)

        val openingInstant = Instant.parse("${today}T$openingTimeString")
        val closingInstant = Instant.parse("${today}T$closingTimeString")

        val openingTime = openingInstant.atZone(zone)
        var closingTime = closingInstant.atZone(zone)

        if (closingTime.isBefore(openingTime)) {
            closingTime = closingTime.plusDays(1)
        }

        val currentTime = ZonedDateTime.now(zone)

        return currentTime.isAfter(openingTime) && currentTime.isBefore(closingTime)
    }

    fun convertProductListToOrderList(productList: List<Product>): List<Order> {
        val orderList = mutableListOf<Order>()

        productList.forEach { product ->
            orderList.add(Order().apply {
                id = product.id
                name = product.name
                price = product.price
                imageUrl = product.imageUrl
            })
        }

        for (selectedOrder in selectedOrderList) {
            for (order in orderList) {
                if (selectedOrder.id == order.id) {
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
            if (selectedOrder.id == order.id) {
                if (order.amount == 0) {
                    _selectedOrderList.remove(selectedOrder)
                } else {
                    selectedOrder.amount = order.amount
                }
                return
            }
        }

        if (order.amount > 0) {
            // Add new order to the list
            _selectedOrderList.add(
                Order(
                    id = order.id,
                    name = order.name,
                    price = order.price,
                    imageUrl = order.imageUrl,
                    amount = order.amount
                )
            )

            // Sort by name when new order added
            _selectedOrderList.sortBy { it.id }
        }
    }
}
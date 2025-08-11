package com.example.omisechallenge.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.Store
import com.example.omisechallenge.domain.usecase.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
) : ViewModel() {
    private var _storeState = MutableStateFlow<Store?>(null)
    val storeState: StateFlow<Store?> = _storeState

    private var _productState = MutableStateFlow<List<Product>>(listOf())
    val productState: StateFlow<List<Product>> = _productState

    init {
//        getStoreInfo()
    }

    fun getStoreInfo() {
        viewModelScope.launch {
            try {
                val storeInfo = storeUseCase.getStoreInfo()
                _storeState.value = storeInfo
            } catch (e: Exception) {
                e.message.toString()
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            try {
                val products = storeUseCase.getProducts()
                _productState.value = products
            } catch (e: Exception) {
                e.message.toString()
            }
        }
    }
}
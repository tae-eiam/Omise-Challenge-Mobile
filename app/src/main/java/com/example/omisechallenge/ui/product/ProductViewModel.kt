package com.example.omisechallenge.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omisechallenge.domain.usecase.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
): ViewModel() {

    init {
        viewModelScope.launch {
            getStoreInfo()
        }
    }

    suspend fun getStoreInfo() {
        val storeInfo = storeUseCase.getStoreInfo().toString()
    }
}
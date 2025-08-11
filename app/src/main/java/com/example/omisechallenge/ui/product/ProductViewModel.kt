package com.example.omisechallenge.ui.product

import android.util.Log
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
        Log.d("Test", "init")
        viewModelScope.launch {
            Log.d("Test", "before get store")
            getStoreInfo()
        }
    }

    suspend fun getStoreInfo() {
        Log.d("Test", "inside get store")
        val storeInfo = storeUseCase.getStoreInfo().toString()
        Log.d("Test", storeInfo)
    }
}
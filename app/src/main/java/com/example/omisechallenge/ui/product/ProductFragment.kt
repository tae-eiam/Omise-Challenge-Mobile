package com.example.omisechallenge.ui.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.omisechallenge.databinding.FragmentProductBinding
import com.example.omisechallenge.ui.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment: Fragment() {
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
        subscribeToEvent()
        configViews()
    }

    private fun initViews() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    private fun initListeners() {

    }

    private fun subscribeToEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.storeState.collectLatest { state ->
                    when(state) {
                        is UiState.Loading -> Unit
                        is UiState.Success -> Unit
                        is UiState.Error -> { binding.tvError.isVisible = true }
                    }
                }

                viewModel.productState.collectLatest { state ->
                    when(state) {
                        is UiState.Loading -> Unit
                        is UiState.Success -> Unit
                        is UiState.Error -> { binding.tvError.isVisible = true }
                    }
                }
            }
        }
    }

    private fun configViews() {
        viewModel.loadStoreInfo()
        viewModel.loadProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
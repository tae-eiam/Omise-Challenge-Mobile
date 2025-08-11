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
import com.example.omisechallenge.R
import com.example.omisechallenge.databinding.FragmentProductBinding
import com.example.omisechallenge.domain.model.Store
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

    private fun configViews() {
        viewModel.loadStoreInfo()
//        viewModel.loadProducts()
    }

    private fun configStoreInfo(info: Store) {
        with(binding) {
            tvStoreName.text = info.name
            tvRating.text = info.rating.toString()

            if (viewModel.isStoreOpen(info.openingTime, info.closingTime)) {
                tvOpenDesc.text = getString(R.string.store_open)
                tvOpenDesc.isEnabled = true
            } else {
                tvOpenDesc.text = getString(R.string.store_close)
                tvOpenDesc.isEnabled = false
            }

            tvOpenHours.text = getString(R.string.store_open_hours, viewModel.convertTimeFormat(info.openingTime), viewModel.convertTimeFormat(info.closingTime))
        }
    }

    private fun initListeners() {

    }

    private fun subscribeToEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.storeState.collectLatest { state ->
                    when(state) {
                        is UiState.Loading -> Unit
                        is UiState.Success -> { configStoreInfo(state.data) }
                        is UiState.Error -> {
                            binding.clStoreInfo.isVisible = false
                            binding.tvError.isVisible = true
                        }
                    }
                }

                viewModel.productState.collectLatest { state ->
                    when(state) {
                        is UiState.Loading -> Unit
                        is UiState.Success -> Unit
                        is UiState.Error -> {
                            binding.clStoreInfo.isVisible = false
                            binding.tvError.isVisible = true
                        }
                    }
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
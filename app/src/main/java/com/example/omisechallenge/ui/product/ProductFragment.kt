package com.example.omisechallenge.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.omisechallenge.R
import com.example.omisechallenge.common.Constant
import com.example.omisechallenge.databinding.FragmentProductBinding
import com.example.omisechallenge.domain.model.Product
import com.example.omisechallenge.domain.model.Store
import com.example.omisechallenge.ui.BaseFragment
import com.example.omisechallenge.ui.UiState
import com.example.omisechallenge.ui.product.adapter.ProductAdapter
import com.example.omisechallenge.ui.product.listener.OnEventTypeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment: BaseFragment() {
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()

    private val productAdapter by lazy { ProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initViews(view: View) {
        initToolbar()
        initProductList()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    private fun initProductList() {
        with(binding) {
            rvStoreProduct.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = productAdapter
            }
        }
    }

    override fun configViews() {
        viewModel.loadStoreInfo()
        viewModel.loadProducts()
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

    private fun configProducts(products: List<Product>) {
        productAdapter.updateData(viewModel.convertProductListToOrderList(products))
    }

    override fun initListeners() {
        productAdapter.setOnProductEventTypeListener { onEventTypeListener ->
            when(onEventTypeListener) {
                is OnEventTypeListener.OnAddOrderClickListener -> {
                    viewModel.updateOrder(onEventTypeListener.order)
                }
                is OnEventTypeListener.OnRemoveOrderClickListener -> {
                    viewModel.updateOrder(onEventTypeListener.order)
                }
            }
        }

        binding.btnNext.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelableArrayList(Constant.ARGUMENT_ORDER_LIST, ArrayList(viewModel.selectedOrderList))
            }
            navigateTo(R.id.action_productFragment_to_summaryFragment, bundle)
        }
    }

    override fun subscribeToEvents() {
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
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productState.collectLatest { state ->
                    when(state) {
                        is UiState.Loading -> Unit
                        is UiState.Success -> {
                            configProducts(state.data)
                        }
                        is UiState.Error -> {
                            binding.clStoreInfo.isVisible = false
                            binding.tvError.isVisible = true
                        }
                    }
                }
            }
        }
    }
}
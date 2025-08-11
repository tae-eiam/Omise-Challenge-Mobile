package com.example.omisechallenge.ui.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.omisechallenge.databinding.FragmentProductBinding
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
                viewModel.storeState.collectLatest {
                    with(binding) {
                        tvStore.text = it?.toString()
                    }
                }

                viewModel.productState.collectLatest {
                    with(binding) {
                        tvProduct.text = it.toString()
                    }
                }
            }
        }
    }

    private fun configViews() {
        viewModel.toString()
//        viewModel.getStoreInfo()
//        viewModel.getProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
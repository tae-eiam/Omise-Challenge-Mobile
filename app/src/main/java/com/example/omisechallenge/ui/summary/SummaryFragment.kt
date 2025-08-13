package com.example.omisechallenge.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.omisechallenge.R
import com.example.omisechallenge.common.Constant
import com.example.omisechallenge.databinding.FragmentSummaryBinding
import com.example.omisechallenge.ui.BaseFragment
import com.example.omisechallenge.ui.UiState
import com.example.omisechallenge.ui.model.Order
import com.example.omisechallenge.ui.summary.adapter.SummaryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SummaryFragment : BaseFragment() {
    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SummaryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initArguments() {
        arguments?.let {
            val orderList = arguments?.getParcelableArrayList<Order>(Constant.ARGUMENT_ORDER_LIST)
            viewModel.orderList = orderList as List<Order>
        }
    }

    override fun initViews(view: View) {
        initToolbar()
        initSummaryContent()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    private fun initSummaryContent() {
        with(binding) {
            rvContent.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = SummaryAdapter(viewModel.orderList).apply {
                    setOnAddressTextChangedListener { text ->
                        viewModel.address = text
                        configConfirmButton()
                    }
                }
            }
        }
    }

    override fun initListeners() {
        binding.btnConfirm.setOnClickListener {
            viewModel.validatePayment()
        }
    }

    override fun subscribeToEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.paymentState.collectLatest { state ->
                    when(state) {
                        is UiState.Idle -> Unit
                        is UiState.Loading -> {
                            binding.pgbLoading.show()
                        }
                        is UiState.Success -> {
                            binding.pgbLoading.hide()
                            navigateTo(R.id.action_summaryFragment_to_successFragment)
                        }
                        is UiState.Error -> {
                            binding.pgbLoading.hide()
                        }
                    }
                }
            }
        }
    }

    override fun configViews() {
        binding.tvTotalValue.text = getString(R.string.total_price, viewModel.getTotalPayment().toString())
        configConfirmButton()
    }

    private fun configConfirmButton() {
        binding.btnConfirm.isEnabled = viewModel.address.isNotEmpty()
    }
}
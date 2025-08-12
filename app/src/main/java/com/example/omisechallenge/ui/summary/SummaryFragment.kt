package com.example.omisechallenge.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.omisechallenge.common.Constant
import com.example.omisechallenge.databinding.FragmentSummaryBinding
import com.example.omisechallenge.ui.BaseFragment
import com.example.omisechallenge.ui.model.Order
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryFragment: BaseFragment() {
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

    override fun initViews(view: View) {
        val a = arguments?.getParcelableArrayList<Order>(Constant.ARGUMENT_ORDER_LIST)

        binding.tvText.text = a.toString()
    }

    override fun initListeners() {

    }

    override fun subscribeToEvents() {

    }

    override fun configViews() {

    }
}
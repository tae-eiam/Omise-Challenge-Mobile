package com.example.omisechallenge.ui.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.omisechallenge.R
import com.example.omisechallenge.databinding.FragmentSuccessBinding
import com.example.omisechallenge.ui.BaseFragment

class SuccessFragment: BaseFragment() {
    private var _binding: FragmentSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initViews(view: View) {
        initToolbar()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    override fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // To prevent back to the previous screen
                    return
                }
            }
        )

        binding.tvContinueOrdering.setOnClickListener {
            navigateTo(R.id.action_successFragment_to_productFragment)
        }

    }

    override fun subscribeToEvents() {
        // no op
    }

    override fun configViews() {
        // no op
    }
}
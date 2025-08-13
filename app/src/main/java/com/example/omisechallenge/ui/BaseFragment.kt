package com.example.omisechallenge.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.omisechallenge.R

abstract class BaseFragment: Fragment() {
    private val navController by lazy {
        requireActivity().findNavController(R.id.content_fragment)
    }

    protected open fun initArguments() {
        // no op
    }

    abstract fun initViews(view: View)
    abstract fun initListeners()
    abstract fun subscribeToEvents()
    abstract fun configViews()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        initViews(view)
        initListeners()
        subscribeToEvents()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        configViews()
    }

    protected open fun navigateTo(@IdRes actionId: Int, args: Bundle? = null) {
        navController.navigate(actionId, args)
    }
}
package com.lovelessgeek.housemanager.base

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BindingFragment<B : ViewDataBinding> : BaseFragment() {
    lateinit protected var binding: B

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)
            ?: throw IllegalStateException("binding is null.")
    }
}
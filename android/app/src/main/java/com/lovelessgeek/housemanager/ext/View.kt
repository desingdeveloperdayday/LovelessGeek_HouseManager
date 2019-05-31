package com.lovelessgeek.housemanager.ext

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * helpers for inflating views
 */

fun <T : ViewDataBinding> ViewGroup.inflateBinding(@LayoutRes layoutId: Int, initializer: T.() -> Unit = {}): T {
    return DataBindingUtil.inflate<T>(
        LayoutInflater.from(context),
        layoutId,
        this,
        false
    ).apply(initializer)
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, initializer: View.() -> Unit = {}): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false).apply(initializer)
}

fun Context.inflate(@LayoutRes layoutId: Int, initializer: View.() -> Unit = {}): View {
    return LayoutInflater.from(this).inflate(layoutId, null, false).apply(initializer)
}

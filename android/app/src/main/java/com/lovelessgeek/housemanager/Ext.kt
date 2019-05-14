package com.lovelessgeek.housemanager

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(): T {
    return ViewModelProviders.of(this).get(T::class.java)
}

// Run and forget about it. Should not be used because of several leaks.
fun runAndForget(block: () -> Unit) {
    Thread(block).start()
}
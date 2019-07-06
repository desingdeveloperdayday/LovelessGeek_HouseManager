package com.lovelessgeek.housemanager.ext

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:goneIf")
fun View.goneIf(gone: Boolean) {
    visibility = if (gone) View.GONE else View.VISIBLE
}

@BindingAdapter("bind:invisibleIf")
fun View.invisibleIf(gone: Boolean) {
    visibility = if (gone) View.INVISIBLE else View.VISIBLE
}

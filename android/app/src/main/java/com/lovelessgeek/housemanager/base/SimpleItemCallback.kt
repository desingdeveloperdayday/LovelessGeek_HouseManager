package com.lovelessgeek.housemanager.base

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.DiffUtil

/**
 * Define basic behavior for DiffUtil.ItemCallback.
 *
 * Expect that the type parameter is data class, or implements equals() for equality checks.
 */
open class SimpleItemCallback<T : Any>(
    private val keySelector: (T).() -> (Any)
) : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) =
        keySelector(oldItem) == keySelector(newItem)

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T) =
        oldItem == newItem
}
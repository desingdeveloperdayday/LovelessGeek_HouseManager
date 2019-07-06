package com.lovelessgeek.housemanager.ext

import androidx.recyclerview.widget.RecyclerView
import com.lovelessgeek.housemanager.ui.MarginItemDecoration

/**
 * Set up margins for items in RecyclerView.
 */
fun RecyclerView.setItemMargin(marginInDp: Int) {
    for (i in 0 until itemDecorationCount) {
        val decoration = getItemDecorationAt(i)
        if (decoration is MarginItemDecoration) {
            removeItemDecoration(decoration)
            break
        }
    }

    addItemDecoration(MarginItemDecoration(marginInDp.toPx(context)))
}

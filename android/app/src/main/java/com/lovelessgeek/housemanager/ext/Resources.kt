package com.lovelessgeek.housemanager.ext

import android.content.Context
import android.util.TypedValue

fun Int.toPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

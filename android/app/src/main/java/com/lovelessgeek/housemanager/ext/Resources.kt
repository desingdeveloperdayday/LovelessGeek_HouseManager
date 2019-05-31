package com.lovelessgeek.housemanager.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Int.toPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)
}
package com.lovelessgeek.housemanager

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lovelessgeek.housemanager.base.BaseActivity

fun BaseActivity.replaceWhenNotCurrent(@IdRes containerId: Int, fragment: Fragment) {
    replaceWhen(containerId, fragment) {
        currentFragment?.javaClass != fragment::class.java
    }
}

fun FragmentActivity.replaceWhen(
    @IdRes containerId: Int, fragment: Fragment,
    predicate: () -> Boolean
) {
    if (predicate())
        replace(containerId, fragment)
}

fun FragmentActivity.replace(@IdRes containerId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .replace(containerId, fragment)
        .commitNow()
}

fun Fragment.setSupportActionBar(toolbar: Toolbar) {
    (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
}

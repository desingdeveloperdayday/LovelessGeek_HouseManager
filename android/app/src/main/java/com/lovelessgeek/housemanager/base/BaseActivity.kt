package com.lovelessgeek.housemanager.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseActivity : AppCompatActivity() {

    protected open val logTag: String = this::class.java.simpleName

    val currentFragment: Fragment?
        get() = supportFragmentManager.fragments.firstOrNull { it?.isVisible == true }
}
package com.lovelessgeek.housemanager

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    protected open val logTag: String = this::class.java.simpleName
}
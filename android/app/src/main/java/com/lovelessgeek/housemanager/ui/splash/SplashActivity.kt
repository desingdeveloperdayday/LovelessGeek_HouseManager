package com.lovelessgeek.housemanager.ui.splash

import android.content.Intent
import android.os.Bundle
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.base.BaseActivity
import com.lovelessgeek.housemanager.ui.main.MainActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

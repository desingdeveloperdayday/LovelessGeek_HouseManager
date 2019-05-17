package com.lovelessgeek.housemanager.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.observe
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.base.BaseActivity
import com.lovelessgeek.housemanager.replace
import com.lovelessgeek.housemanager.ui.login.LoginActivity
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val vm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showNotificationFragment()

        vm.backToLogin.observe(this) {
            it.runIfNotHandled {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.main_notifiation -> showNotificationFragment()
        }
        return true
    }

    private fun showNotificationFragment() {
        if (currentFragment is NotificationFragment) return
        replace(R.id.fragment_container, NotificationFragment())
    }
}

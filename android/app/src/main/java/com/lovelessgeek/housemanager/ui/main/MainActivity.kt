package com.lovelessgeek.housemanager.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.observe
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.base.BaseActivity
import com.lovelessgeek.housemanager.replaceWhenNotCurrent
import com.lovelessgeek.housemanager.ui.login.LoginActivity
import com.lovelessgeek.housemanager.ui.main.notification.NotificationFragment
import com.lovelessgeek.housemanager.ui.main.profile.ProfileFragment
import com.lovelessgeek.housemanager.ui.main.report.ReportFragment
import com.lovelessgeek.housemanager.ui.main.setting.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val vm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()

        observeState()

        showNotificationFragment()
    }

    private fun setupNavigation() {
        main_navigation.setNavigationItemSelectedListener(this::handleNavMenuItem)
    }

    private fun observeState() {
        vm.backToLogin.observe(this) {
            it.runIfNotHandled {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun handleNavMenuItem(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_notifiation -> showNotificationFragment()
            R.id.main_profile -> showProfileFragment()
            R.id.main_report -> showReportFragment()
            R.id.main_settings -> showSettingsFragment()
            else -> return false
        }

        main_drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showNotificationFragment() {
        replaceWhenNotCurrent(
            R.id.fragment_container,
            NotificationFragment().apply {
                onMenuButtonClicked = {
                    findDrawer()?.openDrawer(GravityCompat.START)
                }
            }
        )
    }

    private fun findDrawer() = findViewById<DrawerLayout>(R.id.main_drawer)

    private fun showProfileFragment() {
        replaceWhenNotCurrent(
            R.id.fragment_container,
            ProfileFragment()
        )
    }

    private fun showReportFragment() {
        replaceWhenNotCurrent(
            R.id.fragment_container,
            ReportFragment()
        )
    }

    private fun showSettingsFragment() {
        replaceWhenNotCurrent(
            R.id.fragment_container,
            SettingsFragment()
        )
    }
}

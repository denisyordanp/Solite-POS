package com.socialite.solite_pos.view.opening

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.lifecycleScope
import com.socialite.solite_pos.databinding.ActivityOpeningBinding
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.login.LoginActivity
import com.socialite.solite_pos.view.order_customer.OrderCustomerActivity
import kotlinx.coroutines.launch

class OpeningActivity : SoliteActivity() {

    private lateinit var openingViewModel: OpeningViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityOpeningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openingViewModel = OpeningViewModel.getMainViewModel(this)

        setupVersion(binding)
        preparingApp()
    }

    private fun setupVersion(binding: ActivityOpeningBinding) {
        try {
            binding.tvOpeningVersion.text.apply {
                packageManager.getPackageInfo(packageName, 0).versionName
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    private fun preparingApp() {
        lifecycleScope.launch {
            openingViewModel.fetchRemoteConfig()
            Handler(Looper.getMainLooper()).postDelayed({ checkUser() }, 1000)
        }
    }

    private fun checkUser() {
        if (openingViewModel.isServerActive().not()) {
            toMain()
            return
        }

        if (openingViewModel.isLoggedIn()) {
            toMain()
        } else {
            toLogin()
        }
    }

    private fun toMain() {
        val intent = Intent(this, OrderCustomerActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

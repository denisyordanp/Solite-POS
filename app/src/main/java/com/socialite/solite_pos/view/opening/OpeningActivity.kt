package com.socialite.solite_pos.view.opening

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.socialite.solite_pos.databinding.ActivityOpeningBinding
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.login.LoginActivity
import com.socialite.solite_pos.view.order_customer.OrderCustomerActivity
import kotlinx.coroutines.launch

class OpeningActivity : SoliteActivity() {

    companion object {
        private const val IN_APP_UPDATE_REQUEST_CODE = 1234
    }

    private val openingViewModel: OpeningViewModel by viewModels { OpeningViewModel.getFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityOpeningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupVersion(binding)
        inAppUpdate()
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

    private fun inAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask
            .addOnSuccessListener { appUpdateInfo ->
                if (
                    appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) &&
                    appUpdateInfo.updatePriority() >= 4
                ) {
                    requestInAppUpdate(appUpdateManager, appUpdateInfo)
                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    requestInAppUpdate(appUpdateManager, appUpdateInfo)
                } else {
                    preparingApp()
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                preparingApp()
            }
    }

    private fun requestInAppUpdate(
        appUpdateManager: AppUpdateManager,
        appUpdateInfo: AppUpdateInfo
    ) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            AppUpdateType.IMMEDIATE,
            this,
            IN_APP_UPDATE_REQUEST_CODE
        )
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

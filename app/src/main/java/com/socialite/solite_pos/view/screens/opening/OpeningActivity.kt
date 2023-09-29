package com.socialite.solite_pos.view.screens.opening

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAlertDialog
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.screens.login.LoginActivity
import com.socialite.solite_pos.view.screens.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OpeningActivity : SoliteActivity() {

    private val openingViewModel: OpeningViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val versionName = getVersionName()

        setContent {
            SolitePOSTheme {
                var isUpdateAlertShow by remember { mutableStateOf(false) }

                LaunchedEffect(key1 = Unit) {
                    inAppUpdate(
                        showUpdateSuggestionDialog = {
                            if (!isUpdateAlertShow) isUpdateAlertShow = true
                        }
                    )
                }

                OpeningContent(versionName)

                if (isUpdateAlertShow) {
                    UpdateAppSuggestionAlert()
                }
            }
        }
    }

    @Composable
    private fun OpeningContent(version: String) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .width(300.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.solite),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = version)
            }
        }
    }

    @Composable
    private fun UpdateAppSuggestionAlert() {
        val desc = buildAnnotatedString {
            val bold = SpanStyle(fontWeight = FontWeight.Bold)
            withStyle(bold) {
                append("Sangat disarankan")
            }
            append(" untuk update versi terbaru demi kenyamanan menggunakan aplikasi, setiap update terbaru kemungkinan ")
            withStyle(bold) {
                append("meningkatkan performa dan/atau memperbaiki masalah")
            }
            append(" yang kemungkinan anda alami saat menggunakan aplikasi. Apakah anda mau update sekarang?")
        }
        BasicAlertDialog(
            titleText = stringResource(R.string.new_app_version_available),
            descAnnotatedString = desc,
            positiveAction = {
                openSolitePlayStore()
            },
            positiveText = stringResource(R.string.update_now),
            negativeText = stringResource(R.string.later),
            negativeAction = {
                preparingApp()
            }
        )
    }

    private fun getVersionName(): String {
        return try {
            packageManager.getPackageInfoCompat(packageName).versionName
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            ""
        }
    }

    private fun PackageManager.getPackageInfoCompat(
        packageName: String,
        flags: Int = 0
    ): PackageInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
        }

    private fun inAppUpdate(
        showUpdateSuggestionDialog: () -> Unit
    ) {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask
            .addOnSuccessListener { appUpdateInfo ->
                if (
                    appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    requestInAppUpdate(appUpdateManager, appUpdateInfo, showUpdateSuggestionDialog)
                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    requestInAppUpdate(appUpdateManager, appUpdateInfo, showUpdateSuggestionDialog)
                } else {
                    preparingApp()
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                preparingApp()
            }
    }

    private val requestUpdateResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_CANCELED) {
                finish()
            }
        }

    private fun requestInAppUpdate(
        appUpdateManager: AppUpdateManager,
        appUpdateInfo: AppUpdateInfo,
        shouldShowUpdateSuggestion: () -> Unit
    ) {
        val availableVersion = appUpdateInfo.availableVersionCode()
        if (availableVersion % 5 == 0) {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                requestUpdateResult,
                AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE),
            )
        } else {
            shouldShowUpdateSuggestion()
        }
    }

    private fun openSolitePlayStore() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    private fun preparingApp() {
        openingViewModel.fetchRemoteConfig {
            checkUser()
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

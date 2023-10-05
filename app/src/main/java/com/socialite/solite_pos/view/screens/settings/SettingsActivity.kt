package com.socialite.solite_pos.view.screens.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.socialite.common.utility.state.ErrorState
import com.socialite.domain.helper.DateUtils
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAlertDialog
import com.socialite.solite_pos.compose.FullScreenLoadingView
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.screens.opening.OpeningActivity
import com.socialite.solite_pos.view.screens.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.screens.orders.OrdersActivity
import com.socialite.solite_pos.view.screens.store.StoreActivity
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : SoliteActivity() {
    private val settingViewModel: SettingViewModel by viewModels()

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentDate = DateUtils.currentDate
        settingViewModel.getBadges(currentDate)

        setContent {
            SolitePOSTheme {
                val state = settingViewModel.viewState.collectAsState().value
                SettingContent(state = state)
                SynchronizeAlert(state = state)
            }
        }
    }

    @Composable
    @ExperimentalMaterialApi
    private fun SettingContent(
        state: SettingViewState
    ) {
        FullScreenLoadingView(isLoading = state.isLoading) {
            SettingsMainMenu(
                badges = state.badges,
                isDarkMode = state.isDarkMode,
                isServerActive = state.isServerActive,
                onGeneralMenuClicked = {
                    when (it) {
                        GeneralMenus.NEW_ORDER -> goToOrderCustomerActivity()
                        GeneralMenus.ORDERS -> goToOrdersActivity()
                        GeneralMenus.STORE -> goToStoreActivity()
                        else -> {
                            // Do nothing
                        }
                    }
                },
                onDarkModeChange = {
                    settingViewModel.setDarkMode(it)
                    val delegate = if (it) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    }

                    AppCompatDelegate.setDefaultNightMode(delegate)
                    reLaunchSettingActivity()
                },
                onSynchronizeClicked = {
                    settingViewModel.beginSynchronize()
                },
                onLogout = {
                    settingViewModel.logout()
                    goToOpening()
                }
            )
        }
    }

    @Composable
    private fun SynchronizeAlert(
        state: SettingViewState
    ) {
        if (state.isSynchronizeSuccess || state.error != null) {
            val title =
                if (state.isSynchronizeSuccess) stringResource(R.string.synchronization_success_title) else stringResource(
                    state.error!!.title
                )
            val message =
                if (state.isSynchronizeSuccess) stringResource(R.string.synchronization_success_message) else state.error!!.createMessage(
                    LocalContext.current
                )
            BasicAlertDialog(
                titleText = title,
                descText = message,
                positiveAction = {
                    handleDismissAlert(state.error)
                },
                positiveText = stringResource(R.string.yes),
                onDismiss = {
                    handleDismissAlert(state.error)
                }
            )
        }
    }

    private fun handleDismissAlert(error: ErrorState?) {
        if (error is ErrorState.DeactivatedAccount) {
            settingViewModel.logout()
            goToOpening()
        } else {
            settingViewModel.resetSynchronizeStatus()
        }
    }

    private fun goToOpening() {
        val intent = Intent(this, OpeningActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToOrderCustomerActivity() {
        val intent = Intent(this, OrderCustomerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToOrdersActivity() {
        val intent = Intent(this, OrdersActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToStoreActivity() {
        val intent = Intent(this, StoreActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun reLaunchSettingActivity() {
        startActivity(intent)
        finish()
    }
}

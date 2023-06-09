package com.socialite.solite_pos.view.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAlertDialog
import com.socialite.solite_pos.compose.FullScreenLoadingView
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.opening.OpeningActivity
import com.socialite.solite_pos.view.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.orders.OrdersActivity
import com.socialite.solite_pos.view.store.StoreActivity
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.OrderViewModel

class SettingsActivity : SoliteActivity() {

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var settingViewModel: SettingViewModel

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderViewModel = OrderViewModel.getOrderViewModel(this)
        settingViewModel = SettingViewModel.getMainViewModel(this)

        setContent {
            SolitePOSTheme {
                val state = settingViewModel.viewState.collectAsState().value

                FullScreenLoadingView(isLoading = state.isLoading) {
                    val date = DateUtils.currentDate

                    SettingsMainMenu(
                        orderViewModel = orderViewModel,
                        isDarkMode = state.isDarkMode,
                        isServerActive = state.isServerActive,
                        currentDate = date,
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

                if (state.isSynchronizeSuccess || state.error != null) {
                    val title =
                        if (state.isSynchronizeSuccess) stringResource(R.string.synchronization_success_title) else stringResource(
                            R.string.synchronization_failed_title
                        )
                    val message =
                        if (state.isSynchronizeSuccess) stringResource(R.string.synchronization_success_message) else stringResource(
                            R.string.synchronization_failed_message, state.error?.message ?: ""
                        )
                    BasicAlertDialog(
                        titleText = title,
                        descText = message,
                        positiveAction = {
                            settingViewModel.resetSynchronizeStatus()
                        },
                        positiveText = stringResource(R.string.yes),
                        onDismiss = {
                            settingViewModel.resetSynchronizeStatus()
                        }
                    )
                }
            }
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

package com.socialite.solite_pos.view.screens.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.socialite.solite_pos.view.screens.login.forgot_password.ForgotPasswordScreen
import com.socialite.solite_pos.view.screens.login.login.LoginScreen
import com.socialite.solite_pos.view.screens.login.register.RegisterScreen
import com.socialite.solite_pos.view.screens.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SolitePOSTheme {
                MainContent()
            }
        }
    }

    @Composable
    fun MainContent() {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = LoginDestinations.LOGIN,
        ) {
            composable(
                route = LoginDestinations.LOGIN
            ) {
                LoginScreen(
                    onRegister = {
                        navController.navigate(LoginDestinations.REGISTER)
                    },
                    onSuccessLogin = {
                        toMain()
                    },
                    onForgotPassword = {
                        navController.navigate(LoginDestinations.FORGOT_PASSWORD)
                    }
                )
            }

            composable(
                route = LoginDestinations.REGISTER
            ) {
                RegisterScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onSuccessLogin = {
                        toMain()
                    }
                )
            }

            composable(LoginDestinations.FORGOT_PASSWORD) {
                ForgotPasswordScreen {
                    navController.navigateUp()
                }
            }
        }
    }

    private fun toMain() {
        val intent = Intent(this, OrderCustomerActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

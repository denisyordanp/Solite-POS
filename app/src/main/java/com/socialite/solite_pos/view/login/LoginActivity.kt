package com.socialite.solite_pos.view.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.socialite.solite_pos.compose.FullScreenLoadingView
import com.socialite.solite_pos.view.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme

class LoginActivity : ComponentActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = LoginViewModel.getOrderViewModel(this)

        setContent {
            SolitePOSTheme {
                MainContent()
            }
        }
    }

    @Composable
    fun MainContent() {

        val state = viewModel.viewState.collectAsState().value

        if (state.isSuccessLogin) toMain()

        FullScreenLoadingView(
            isLoading = state.isLoading
        ) {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = LoginDestinations.LOGIN,
            ) {
                composable(
                    route = LoginDestinations.LOGIN
                ) {
                    LoginScreen(
                        errorMessage = state.errorMessage,
                        onLogin = { email, password ->
                            viewModel.login(email, password)
                        },
                        onRegister = {
                            navController.navigate(LoginDestinations.REGISTER)
                        }
                    )
                }
                composable(
                    route = LoginDestinations.REGISTER
                ) {
                    LaunchedEffect(key1 = Unit) {
                        viewModel.resetState()
                    }

                    RegisterScreen(
                        errorMessage = state.errorMessage,
                        onBackClick = {
                            navController.navigateUp()
                        },
                        onRegister = { name, email, password, store ->
                            viewModel.register(
                                name = name,
                                email = email,
                                password = password,
                                storeName = store
                            )
                        }
                    )
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

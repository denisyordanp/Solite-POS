package com.socialite.solite_pos.view.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.socialite.solite_pos.view.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            SolitePOSTheme {
                NavHost(
                    navController = navController,
                    startDestination = LoginDestinations.LOGIN,
                ) {
                    composable(
                        route = LoginDestinations.LOGIN
                    ) {
                        var isError by remember { mutableStateOf(false) }
                        LoginScreen(
                            isError = isError,
                            onLogin = { email, password ->
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            isError = true
                                            toMain()
                                        } else {
                                            isError = true
                                        }
                                    }
                            },
                            onRegister = {
                                navController.navigate(LoginDestinations.REGISTER)
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
                            onRegister = { email, password, store ->
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener {

                                    }
                            }
                        )
                    }
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

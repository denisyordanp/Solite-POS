package com.socialite.solite_pos.view.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.PrimaryButtonView

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    errorMessage: String?,
    onLogin: (email: String, password: String) -> Unit,
    onRegister: () -> Unit
) {
    Surface(
        color = MaterialTheme.colors.primary
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var passwordVisible by remember { mutableStateOf(false) }

            Image(
                modifier = Modifier
                    .weight(1f),
                painter = painterResource(id = R.drawable.solite),
                contentDescription = null
            )
            if (errorMessage.isNullOrEmpty().not()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .background(
                            color = MaterialTheme.colors.surface.copy(
                                0.3f
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = errorMessage!!,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            BasicEditText(
                value = email,
                keyboardType = KeyboardType.Email,
                placeHolder = stringResource(R.string.enter_email),
                onValueChange = {
                    email = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            BasicEditText(
                value = password,
                keyboardType = KeyboardType.Password,
                passwordVisible = passwordVisible,
                visualTransformation = PasswordVisualTransformation(),
                placeHolder = stringResource(R.string.enter_password),
                onValueChange = {
                    password = it
                },
                onPasswordVisibility = {
                    passwordVisible = !passwordVisible
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButtonView(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                isEnabled = email.isNotEmpty() && password.isNotEmpty(),
                buttonText = stringResource(R.string.login),
                backgroundColor = MaterialTheme.colors.surface,
                onClick = {
                    onLogin(email, password)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButtonView(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                backgroundColor = MaterialTheme.colors.surface,
                buttonText = stringResource(R.string.register),
                onClick = { onRegister() }
            )
        }
    }
}
package com.socialite.solite_pos.view.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.utils.config.PasswordStatus
import com.socialite.solite_pos.utils.config.isNotValidEmail
import com.socialite.solite_pos.utils.config.isNotValidPassword

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    errorMessage: String?,
    onRegister: (
        name: String,
        email: String,
        password: String,
        store: String
    ) -> Unit
) {
    Scaffold(
        topBar = {
            BasicTopBar(
                titleText = stringResource(R.string.registration_form),
                elevation = 0.dp,
                onBackClicked = onBackClick
            )
        },
        content = { padding ->
            Surface(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                color = MaterialTheme.colors.primary
            ) {
                var name by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var confirmPassword by remember { mutableStateOf("") }
                var store by remember { mutableStateOf("") }
                val nameErrorMessage by remember {
                    derivedStateOf {
                        when {
                            name.isEmpty() -> R.string.field_can_not_emtpy
                            else -> null
                        }
                    }
                }
                val emailErrorMessage by remember {
                    derivedStateOf {
                        when {
                            email.isEmpty() -> R.string.field_can_not_emtpy
                            email.isNotValidEmail() -> R.string.fill_with_email_correct_format
                            else -> null
                        }
                    }
                }
                val passwordErrorMessage by remember {
                    derivedStateOf {
                        when {
                            password.isEmpty() -> R.string.field_can_not_emtpy
                            else -> when (password.isNotValidPassword()) {
                                PasswordStatus.LENGTH -> R.string.password_must_between_8_15_character
                                PasswordStatus.UPPER_LOWER_CASE -> R.string.password_must_contains_upper_lower_case
                                PasswordStatus.SYMBOL -> R.string.password_must_contains_symbol
                                PasswordStatus.SUCCESS -> null
                            }
                        }
                    }
                }
                val confirmPasswordErrorMessage by remember {
                    derivedStateOf {
                        when {
                            confirmPassword.isEmpty() -> R.string.field_can_not_emtpy
                            confirmPassword != password -> R.string.confirm_password_must_match_with_password
                            else -> null
                        }
                    }
                }
                val storeErrorMessage by remember {
                    derivedStateOf {
                        when {
                            store.isEmpty() -> R.string.field_can_not_emtpy
                            else -> null
                        }
                    }
                }
                val isButtonEnabled by remember {
                    derivedStateOf {
                        emailErrorMessage == null &&
                                passwordErrorMessage == null &&
                                storeErrorMessage == null &&
                                nameErrorMessage == null
                    }
                }

                var passwordVisibility by remember { mutableStateOf(false) }
                var confirmPasswordVisibility by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    BasicEditText(
                        value = name,
                        placeHolder = stringResource(R.string.enter_your_name),
                        onValueChange = {
                            name = it
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    nameErrorMessage?.let {
                        Text(
                            text = stringResource(id = it),
                            color = Color.Red
                        )
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
                    Spacer(modifier = Modifier.height(4.dp))
                    emailErrorMessage?.let {
                        Text(
                            text = stringResource(id = it),
                            color = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    BasicEditText(
                        value = password,
                        keyboardType = KeyboardType.Password,
                        visualTransformation = PasswordVisualTransformation(),
                        passwordVisible = passwordVisibility,
                        placeHolder = stringResource(R.string.enter_password),
                        onValueChange = {
                            password = it
                        },
                        onPasswordVisibility = {
                            passwordVisibility = !passwordVisibility
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    passwordErrorMessage?.let {
                        Text(
                            text = stringResource(id = it),
                            color = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    BasicEditText(
                        value = confirmPassword,
                        keyboardType = KeyboardType.Password,
                        visualTransformation = PasswordVisualTransformation(),
                        passwordVisible = confirmPasswordVisibility,
                        placeHolder = stringResource(R.string.enter_confirm_password),
                        onValueChange = {
                            confirmPassword = it
                        },
                        onPasswordVisibility = {
                            confirmPasswordVisibility = !confirmPasswordVisibility
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    confirmPasswordErrorMessage?.let {
                        Text(
                            text = stringResource(id = it),
                            color = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    BasicEditText(
                        value = store,
                        placeHolder = stringResource(R.string.enter_store_name),
                        onValueChange = {
                            store = it
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    storeErrorMessage?.let {
                        Text(
                            text = stringResource(id = it),
                            color = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
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
                    PrimaryButtonView(
                        modifier = Modifier
                            .fillMaxWidth(),
                        backgroundColor = MaterialTheme.colors.surface,
                        isEnabled = isButtonEnabled,
                        buttonText = stringResource(id = R.string.register),
                        onClick = {
                            onRegister(name, email, password, store)
                        }
                    )
                }
            }
        }
    )
}

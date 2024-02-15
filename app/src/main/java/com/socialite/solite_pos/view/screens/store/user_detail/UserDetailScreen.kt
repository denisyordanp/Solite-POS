package com.socialite.solite_pos.view.screens.store.user_detail

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.socialite.common.utility.state.DataState
import com.socialite.common.utility.state.ErrorState
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.FullScreenLoadingView
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.schema.User
import com.socialite.solite_pos.utils.config.PasswordStatus
import com.socialite.solite_pos.utils.config.isNotValidEmail
import com.socialite.solite_pos.utils.config.isNotValidPassword
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun UserDetailScreen(
    currentViewModel: UserDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val user = currentViewModel.user.collectAsState(initial = null)
    val updateUserState = currentViewModel.updateUserState.collectAsState()
    val changePasswordState = currentViewModel.changePasswordState.collectAsState()
    val isLoading = remember {
        derivedStateOf {
            updateUserState.value is DataState.Loading || changePasswordState.value is DataState.Loading
        }
    }

    val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    if (changePasswordState.value is DataState.Success) {
        LaunchedEffect(key1 = Unit) {
            modalState.hide()
        }
    }

    FullScreenLoadingView(isLoading = isLoading.value) {
        ModalBottomSheetLayout(
            sheetState = modalState,
            sheetShape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            ),
            sheetContent = {
                val changePasswordStateValue = changePasswordState.value
                ChangePasswordBottomSheet(
                    errorState = if (changePasswordStateValue is DataState.Error) changePasswordStateValue.errorState else null,
                    onSubmitPassword = { password, confirmPassword ->
                        currentViewModel.changePassword(password, confirmPassword)
                    },
                )
            },
            content = {
                user.value?.let {
                    val updateUserStateValue = updateUserState.value
                    UserDetailContent(
                        user = it,
                        error = if (updateUserStateValue is DataState.Error) updateUserStateValue.errorState else null,
                        onBackClick = onBackClick,
                        onChangePasswordClicked = {
                            scope.launch {
                                modalState.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        },
                        onEditUser = { name, email ->
                            currentViewModel.updateUser(
                                it.copy(
                                    name = name,
                                    email = email
                                )
                            )
                        }
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun UserDetailContent(
    user: User,
    error: ErrorState?,
    onBackClick: () -> Unit,
    onChangePasswordClicked: () -> Unit,
    onEditUser: (name: String, email: String) -> Unit
) {
    Scaffold(
        topBar = {
            BasicTopBar(
                titleText = stringResource(R.string.edit_data_title),
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
            ) {
                var name by remember { mutableStateOf(user.name) }
                var email by remember { mutableStateOf(user.email) }

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
                val isButtonShow by remember {
                    derivedStateOf {
                        name != user.name || email != user.email
                    }
                }
                val isButtonEnabled by remember {
                    derivedStateOf {
                        emailErrorMessage == null &&
                                nameErrorMessage == null
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (error != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .background(
                                    color = MaterialTheme.colors.primary.copy(
                                        0.3f
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    text = stringResource(error.title),
                                    color = Color.Red,
                                    textAlign = TextAlign.Center,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    text = error.createMessage(LocalContext.current),
                                    color = Color.Red,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
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
                    Spacer(modifier = Modifier.height(24.dp))
                    PrimaryButtonView(
                        modifier = Modifier
                            .fillMaxWidth(),
                        buttonText = stringResource(id = R.string.change_password_title),
                        onClick = onChangePasswordClicked
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedVisibility(
                        visible = isButtonShow
                    ) {
                        PrimaryButtonView(
                            modifier = Modifier
                                .fillMaxWidth(),
                            isEnabled = isButtonEnabled,
                            buttonText = stringResource(id = R.string.save),
                            onClick = {
                                onEditUser(name, email)
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
private fun ChangePasswordBottomSheet(
    errorState: ErrorState?,
    onSubmitPassword: (password: String, confirm: String) -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val newPasswordErrorMessage by remember {
        derivedStateOf {
            when {
                newPassword.isEmpty() -> R.string.field_can_not_emtpy
                else -> when (newPassword.isNotValidPassword()) {
                    PasswordStatus.LENGTH -> R.string.password_must_between_8_15_character
                    PasswordStatus.UPPER_LOWER_CASE -> R.string.password_must_contains_upper_lower_case
                    PasswordStatus.SYMBOL -> R.string.password_must_contains_symbol
                    PasswordStatus.NUMBER -> R.string.password_must_contains_number
                    PasswordStatus.SUCCESS -> null
                }
            }
        }
    }
    val confirmPasswordErrorMessage by remember {
        derivedStateOf {
            when {
                confirmPassword.isEmpty() -> R.string.field_can_not_emtpy
                confirmPassword != newPassword -> R.string.confirm_password_must_match_with_password
                else -> null
            }
        }
    }
    val isButtonEnabled by remember {
        derivedStateOf {
            newPasswordErrorMessage == null &&
                    confirmPasswordErrorMessage == null
        }
    }

    var oldPasswordVisibility by remember { mutableStateOf(false) }
    var newPasswordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.change_password_title),
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (errorState != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(
                        color = MaterialTheme.colors.primary.copy(
                            0.3f
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(id = errorState.title),
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = errorState.createMessage(LocalContext.current),
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        BasicEditText(
            value = oldPassword,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
            passwordVisible = oldPasswordVisibility,
            placeHolder = stringResource(R.string.enter_old_password),
            onValueChange = {
                oldPassword = it
            },
            onPasswordVisibility = {
                oldPasswordVisibility = !oldPasswordVisibility
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        BasicEditText(
            value = newPassword,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
            passwordVisible = newPasswordVisibility,
            placeHolder = stringResource(R.string.enter_new_password),
            onValueChange = {
                newPassword = it
            },
            onPasswordVisibility = {
                newPasswordVisibility = !newPasswordVisibility
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        newPasswordErrorMessage?.let {
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
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButtonView(
            modifier = Modifier
                .fillMaxWidth(),
            isEnabled = isButtonEnabled,
            buttonText = stringResource(id = R.string.save),
            onClick = {
                onSubmitPassword(oldPassword, confirmPassword)
                oldPassword = ""
                newPassword = ""
                confirmPassword = ""
            }
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun Preview() {
    SolitePOSTheme {
        UserDetailScreen {

        }
    }
}
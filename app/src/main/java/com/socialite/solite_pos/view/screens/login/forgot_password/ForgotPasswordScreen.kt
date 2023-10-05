package com.socialite.solite_pos.view.screens.login.forgot_password

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.socialite.common.utility.state.DataState
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAlertDialog
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.FullScreenLoadingView
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.utils.config.isNotValidEmail

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ForgotPasswordScreen(
    currentViewModel: ForgotPasswordViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    var shouldShowSuccessAlert by remember { mutableStateOf(false) }
    var shouldShowSendConfirmationAlert by remember { mutableStateOf(false) }
    val state = currentViewModel.viewState.collectAsState()
    val isAbleToSendState =
        currentViewModel.isAbleSendEmail.collectAsState(initial = DataState.Idle)
    val isAbleToSendEmail = remember {
        derivedStateOf {
            isAbleToSendState.value is DataState.Success
        }
    }
    val error = remember {
        derivedStateOf {
            when {
                isAbleToSendState.value is DataState.Error -> (isAbleToSendState.value as DataState.Error).errorState
                state.value is DataState.Error -> (state.value as DataState.Error).errorState
                else -> null
            }
        }
    }
    if (state.value is DataState.Success) {
        shouldShowSuccessAlert = true
    }
    var email by remember { mutableStateOf("") }

    FullScreenLoadingView(
        isLoading = state.value is DataState.Loading
    ) {
        Scaffold(
            topBar = {
                BasicTopBar(
                    titleText = stringResource(id = R.string.forgot_password_title),
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
                    val emailErrorMessage by remember {
                        derivedStateOf {
                            when {
                                email.isEmpty() -> R.string.field_can_not_emtpy
                                email.isNotValidEmail() -> R.string.fill_with_email_correct_format
                                else -> null
                            }
                        }
                    }
                    val isButtonEnabled by remember {
                        derivedStateOf {
                            emailErrorMessage == null
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        if (error.value != null) {
                            Spacer(modifier = Modifier.height(16.dp))
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
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        modifier = Modifier.padding(8.dp),
                                        text = stringResource(error.value!!.title),
                                        color = Color.Red,
                                        textAlign = TextAlign.Center,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        text = error.value!!.createMessage(LocalContext.current),
                                        color = Color.Red,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                        if (isAbleToSendEmail.value) {
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
                                backgroundColor = MaterialTheme.colors.surface,
                                isEnabled = isButtonEnabled && isAbleToSendEmail.value,
                                buttonText = stringResource(R.string.send_email),
                                onClick = {
                                    shouldShowSendConfirmationAlert = true
                                }
                            )
                        }
                    }
                }
            }
        )

        if (shouldShowSuccessAlert) {
            val text = buildAnnotatedString {
                val bold = SpanStyle(fontWeight = FontWeight.Bold)
                append("Permintaan reset password terkirim ke email ")
                withStyle(bold) {
                    append(email)
                }
                append(". Silahkan periksa ")
                withStyle(bold) {
                    append("Inbox ")
                }
                append("email anda dan jika tidak ada, mohon periksa juga ")
                withStyle(bold) {
                    append("Spam")
                }
                append(". Terima kasih.")
            }
            BasicAlertDialog(
                titleText = stringResource(R.string.email_sent),
                descAnnotatedString = text,
                positiveAction = {
                    shouldShowSuccessAlert = !shouldShowSuccessAlert
                    onBackClick()
                },
                positiveText = stringResource(id = R.string.yes)
            )
        }

        if (shouldShowSendConfirmationAlert) {
            val text = buildAnnotatedString {
                val bold = SpanStyle(fontWeight = FontWeight.Bold)
                append("Anda akan mengirim permintaan reset password ke email ")
                withStyle(bold) {
                    append(email)
                }
                append(". Mohon periksa kembali sebelum mengirim, karena anda tidak akan dapat mengirim permintaan reset password lagi selama ")
                withStyle(bold) {
                    append("30 menit ")
                }
                append("kedepan. Anda yakin email anda benar?")
            }
            BasicAlertDialog(
                titleText = stringResource(R.string.attention_title),
                descAnnotatedString = text,
                positiveAction = {
                    currentViewModel.sendEmail(email)
                    shouldShowSendConfirmationAlert = !shouldShowSendConfirmationAlert
                },
                positiveText = stringResource(id = R.string.yes),
                negativeText = stringResource(id = R.string.cancel),
                negativeAction = {
                    shouldShowSendConfirmationAlert = !shouldShowSendConfirmationAlert
                }
            )
        }
    }
}
package com.socialite.solite_pos.view.screens.login.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.common.utility.state.DataState
import com.socialite.domain.domain.ForgotPassword
import com.socialite.domain.domain.IsAbleSendForgotPassword
import com.socialite.domain.helper.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPassword: ForgotPassword,
    private val isAbleSendForgotPassword: IsAbleSendForgotPassword
) : ViewModel() {

    private val _viewState = MutableStateFlow<DataState<Boolean>>(DataState.Idle)
    val viewState = _viewState.asStateFlow()

    val isAbleSendEmail get() = isAbleSendForgotPassword.invoke(DateUtils.calendar.timeInMillis)

    fun sendEmail(
        email: String,
    ) = viewModelScope.launch {
        _viewState.emitAll(
            forgotPassword(
                email = email,
                currentTime = DateUtils.calendar.timeInMillis
            )
        )
    }
}

package com.socialite.solite_pos.view.screens.login.login

import com.socialite.common.state.ErrorState

data class LoginViewState(
    val errorState: ErrorState?,
    val isLoading: Boolean,
    val isSuccessLogin: Boolean
) {

    fun copyLoading() = this.copy(
        isLoading = true,
        errorState = null
    )

    fun copySucceed() = this.copy(
        errorState = null,
        isLoading = false,
        isSuccessLogin = true
    )

    fun copyError(errorState: ErrorState) = this.copy(
        isLoading = false,
        errorState = errorState
    )

    companion object {
        fun idle() = LoginViewState(
            errorState = null,
            isLoading = false,
            isSuccessLogin = false
        )
    }
}

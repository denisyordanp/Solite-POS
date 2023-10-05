package com.socialite.solite_pos.view.screens.login.register

import com.socialite.common.utility.state.ErrorState

data class RegisterViewState(
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
        fun idle() = RegisterViewState(
            errorState = null,
            isLoading = false,
            isSuccessLogin = false
        )
    }
}

package com.socialite.solite_pos.view.login

data class LoginViewState(
    val errorMessage: String?,
    val isLoading: Boolean,
    val isSuccessLogin: Boolean
) {

    fun copyLoading() = this.copy(
        isLoading = true,
        errorMessage = null
    )

    fun copySucceed() = this.copy(
        errorMessage = null,
        isLoading = false,
        isSuccessLogin = true
    )

    fun copyError(message: String) = this.copy(
        isLoading = false,
        errorMessage = message
    )

    companion object {
        fun idle() = LoginViewState(
            errorMessage = null,
            isLoading = false,
            isSuccessLogin = false
        )
    }
}

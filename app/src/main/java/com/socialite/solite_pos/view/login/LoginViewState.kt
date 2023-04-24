package com.socialite.solite_pos.view.login

data class LoginViewState(
    val errorMessage: String?,
    val isLoading: Boolean,
    val token: String?
) {

    fun copyLoading() = this.copy(
        isLoading = true,
        errorMessage = null
    )

    fun copySuccess(token: String) = this.copy(
        errorMessage = null,
        isLoading = false,
        token = token
    )

    fun copyError(message: String) = this.copy(
        isLoading = false,
        errorMessage = message
    )

    companion object {
        fun idle() = LoginViewState(
            errorMessage = null,
            isLoading = false,
            token = null
        )
    }
}

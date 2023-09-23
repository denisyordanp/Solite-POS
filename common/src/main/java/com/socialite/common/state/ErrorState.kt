package com.socialite.common.state

import android.content.Context
import com.socialite.common.R

sealed class ErrorState(
    open val title: Int,
    open val message: Int,
    open val additionalMessage: String,
    open val throwable: Throwable
) {

    fun createMessage(context: Context): String {
        return context.getString(message, additionalMessage)
    }

    data class NoInternet(
        override val throwable: Throwable
    ) : ErrorState(
        R.string.no_internet_connection_title,
        R.string.no_internet_connection_message,
        "",
        throwable
    )

    data class ServerError(
        override val additionalMessage: String,
        override val throwable: Throwable
    ) : ErrorState(
        R.string.something_wrong_title,
        R.string.something_wrong_message,
        additionalMessage,
        throwable
    )

    data class Timeout(
        override val additionalMessage: String = "",
        override val throwable: Throwable
    ) : ErrorState(
        R.string.timeout_connection_title,
        R.string.no_internet_connection_message,
        additionalMessage,
        throwable
    )

    data class UserError(
        override val additionalMessage: String,
        override val throwable: Throwable
    ) : ErrorState(
        R.string.request_failed,
        R.string.user_mistake_message,
        additionalMessage,
        throwable
    )

    object DeactivatedAccount : ErrorState(
        R.string.deactivated_account_title,
        R.string.deactivated_account_message,
        "",
        IllegalArgumentException()
    )

    data class Unknown(
        override val additionalMessage: String,
        override val throwable: Throwable
    ) : ErrorState(R.string.something_wrong_title, R.string.something_wrong_message, additionalMessage, throwable)
}

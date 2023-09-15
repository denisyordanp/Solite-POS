package com.socialite.common.state

import com.socialite.common.R

sealed class ErrorState(
    open val title: Int,
    open val message: Int,
    open val additionalMessage: String,
    open val throwable: Throwable
) {
    data class NoInternet(
        override val title: Int = R.string.no_internet_connection_title,
        override val message: Int = R.string.no_internet_connection_message,
        override val additionalMessage: String = "",
        override val throwable: Throwable
    ) : ErrorState(title, message, additionalMessage, throwable)

    data class ServerErrorState(
        override val title: Int = R.string.something_wrong_title,
        override val message: Int = R.string.something_wrong_message,
        override val additionalMessage: String,
        override val throwable: Throwable
    ) : ErrorState(title, message, additionalMessage, throwable)

    data class Timeout(
        override val title: Int = R.string.timeout_connection_title,
        override val message: Int = R.string.no_internet_connection_message,
        override val additionalMessage: String = "",
        override val throwable: Throwable
    ) : ErrorState(title, message, additionalMessage, throwable)

    data class Unknown(
        override val title: Int = R.string.something_wrong_title,
        override val message: Int = R.string.something_wrong_message,
        override val additionalMessage: String,
        override val throwable: Throwable
    ) : ErrorState(title, message, additionalMessage, throwable)
}
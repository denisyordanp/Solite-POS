package com.socialite.common.utility.extension

import com.google.gson.reflect.TypeToken
import com.socialite.common.utility.state.ErrorState
import com.socialite.core.network.NetworkConfig
import com.socialite.core.network.response.ApiResponse
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun <T> Throwable.toError(): ErrorState {
    return try {
        when {
            this is IOException && message == "No Internet" -> {
                ErrorState.NoInternet(throwable = this)
            }

            this is HttpException -> {
                val response = this.response()
                val body = response?.errorBody()

                val apiResponse: ApiResponse<T>? = body?.let {
                    val type = object : TypeToken<ApiResponse<T>>() {}.type
                    val gson = NetworkConfig.gson()
                    gson.fromJson(it.string(), type)
                }
                val additionalMessage = apiResponse?.error.orEmpty()

                if (response?.code() in 400..499) {
                    ErrorState.UserError(
                        additionalMessage = additionalMessage,
                        throwable = this
                    )
                } else {
                    ErrorState.ServerError(
                        additionalMessage = additionalMessage,
                        throwable = this
                    )
                }
            }

            this is SocketTimeoutException -> {
                ErrorState.Timeout(throwable = this)
            }

            this is UnknownHostException -> {
                ErrorState.NoInternet(throwable = this)
            }

            else -> {
                ErrorState.Unknown(
                    additionalMessage = this.message.orEmpty(),
                    throwable = this
                )
            }
        }
    } catch (e: Exception) {
        ErrorState.Unknown(
            additionalMessage = this.message.orEmpty(),
            throwable = this
        )
    }
}
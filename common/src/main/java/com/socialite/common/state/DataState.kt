package com.socialite.common.state

sealed class DataState<out T> {
    object Loading : DataState<Nothing>()
    data class Error(val throwable: Throwable) : DataState<Nothing>()

    data class Success<T>(val data: T) : DataState<T>()
}

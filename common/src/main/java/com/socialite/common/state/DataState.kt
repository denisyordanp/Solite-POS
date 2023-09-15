package com.socialite.common.state

sealed class DataState<out T> {
    object Idle : DataState<Nothing>()
    object Loading : DataState<Nothing>()
    data class Error(val errorState: ErrorState) : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
}

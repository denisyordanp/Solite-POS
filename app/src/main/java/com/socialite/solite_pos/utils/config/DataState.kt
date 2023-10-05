package com.socialite.solite_pos.utils.config

import androidx.compose.runtime.Composable
import com.socialite.common.utility.state.DataState
import com.socialite.common.utility.state.ErrorState

@Composable
fun <T> DataState<T>.result(
    onSuccess: @Composable (data: T) -> Unit,
    onError: @Composable (errorState: ErrorState) -> Unit,
    onIdle: @Composable (() -> Unit)? = null,
    onLoading: @Composable (() -> Unit)? = null
) {
    when (this) {
        is DataState.Error -> onError(errorState)
        is DataState.Idle -> onIdle?.invoke()
        is DataState.Loading -> onLoading?.invoke()
        is DataState.Success -> onSuccess(data)
    }
}
package com.socialite.domain.domain

import com.socialite.common.utility.state.DataState
import kotlinx.coroutines.flow.Flow

fun interface IsAbleSendForgotPassword {
    operator fun invoke(currentTime: Long): Flow<DataState<Boolean>>
}
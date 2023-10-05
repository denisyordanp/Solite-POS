package com.socialite.domain.domain

import com.socialite.common.utility.state.DataState
import kotlinx.coroutines.flow.Flow

fun interface ForgotPassword {
    operator fun invoke(email: String, currentTime: Long): Flow<DataState<Boolean>>
}
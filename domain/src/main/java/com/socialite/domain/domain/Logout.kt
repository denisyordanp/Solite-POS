package com.socialite.domain.domain

import com.socialite.common.state.DataState
import kotlinx.coroutines.flow.Flow

fun interface Logout {
    operator fun invoke(): Flow<DataState<Boolean>>
}
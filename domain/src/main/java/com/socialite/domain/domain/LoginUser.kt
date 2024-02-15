package com.socialite.domain.domain

import com.socialite.common.utility.state.DataState
import kotlinx.coroutines.flow.Flow

fun interface LoginUser {
    operator fun invoke(
        email: String,
        password: String
    ): Flow<DataState<Boolean>>
}

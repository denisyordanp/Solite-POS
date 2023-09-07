package com.socialite.domain.domain

import com.socialite.common.state.DataState
import kotlinx.coroutines.flow.Flow

fun interface LoginUser {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Flow<DataState<String>>
}

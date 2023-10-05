package com.socialite.domain.domain

import com.socialite.common.utility.state.DataState
import kotlinx.coroutines.flow.Flow

fun interface RegisterUser {
    operator fun invoke(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): Flow<DataState<Boolean>>
}

package com.socialite.domain.domain

import com.socialite.common.state.DataState
import com.socialite.domain.schema.main.User
import kotlinx.coroutines.flow.Flow

fun interface GetUsers {
    operator fun invoke(): Flow<DataState<List<User>>>
}
package com.socialite.domain.domain

import com.socialite.common.state.DataState
import com.socialite.domain.schema.main.User
import kotlinx.coroutines.flow.Flow

fun interface UpdateUser {
    operator fun invoke(user: User, isLoggedInUser: Boolean): Flow<DataState<Boolean>>
}
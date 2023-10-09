package com.socialite.domain.domain

import com.socialite.common.utility.state.DataState
import com.socialite.schema.ui.main.User
import kotlinx.coroutines.flow.Flow

fun interface FetchUsers {
    operator fun invoke(): Flow<DataState<List<User>>>
}
package com.socialite.domain.domain

import com.socialite.common.utility.state.DataState
import kotlinx.coroutines.flow.Flow

fun interface ChangePassword {
    operator fun invoke(oldPassword: String, newPassword: String): Flow<DataState<Boolean>>
}
package com.socialite.domain.domain

import com.socialite.common.utility.state.DataState
import kotlinx.coroutines.flow.Flow

fun interface Synchronize {
    operator fun invoke(): Flow<DataState<Boolean>>
}

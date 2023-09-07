package com.socialite.data.repository

import com.socialite.common.state.DataState
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun login(
        email: String,
        password: String
    ): Flow<DataState<String>>

    suspend fun register(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): Flow<DataState<String>>
}

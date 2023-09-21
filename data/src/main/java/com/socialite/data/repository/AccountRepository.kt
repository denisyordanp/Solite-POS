package com.socialite.data.repository

import com.socialite.common.network.response.ApiResponse
import com.socialite.data.schema.response.AccountResponse
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun login(
        email: String,
        password: String
    ): Flow<ApiResponse<AccountResponse>>

    fun register(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): Flow<ApiResponse<AccountResponse>>
}

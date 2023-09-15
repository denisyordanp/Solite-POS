package com.socialite.data.repository

import com.socialite.common.network.response.ApiResponse
import com.socialite.data.schema.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun login(
        email: String,
        password: String
    ): Flow<ApiResponse<LoginResponse>>

    fun register(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): Flow<ApiResponse<LoginResponse>>
}

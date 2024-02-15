package com.socialite.data.repository

import com.socialite.core.network.response.ApiResponse
import com.socialite.data.schema.response.SynchronizeParams
import com.socialite.data.schema.response.SynchronizeResponse
import kotlinx.coroutines.flow.Flow

interface SynchronizeRepository {
    fun synchronize(
        param: SynchronizeParams
    ): Flow<ApiResponse<SynchronizeResponse>>
}
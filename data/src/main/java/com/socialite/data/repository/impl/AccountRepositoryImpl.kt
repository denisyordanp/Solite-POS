package com.socialite.data.repository.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.common.network.response.ApiResponse
import com.socialite.data.di.NonAuthorizationService
import com.socialite.data.network.SoliteServices
import com.socialite.data.repository.AccountRepository
import com.socialite.data.schema.response.LoginResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    @NonAuthorizationService private val service: SoliteServices,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : AccountRepository {
    override fun login(email: String, password: String): Flow<ApiResponse<LoginResponse>> {
        return flow {
            val request = service.login(email, password)

            val token = request.data?.token
            check(!token.isNullOrEmpty()) {
                "Token response null or empty"
            }

            emit(request)
        }.flowOn(dispatcher)
    }

    override fun register(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): Flow<ApiResponse<LoginResponse>> {
        return flow {
            val request = service.register(name, email, password, storeName)

            val token = request.data?.token
            check(!token.isNullOrEmpty()) {
                "Token response null or empty"
            }

            emit(request)
        }.flowOn(dispatcher)
    }
}

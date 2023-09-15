package com.socialite.data.repository.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.common.extension.toError
import com.socialite.common.network.response.ApiResponse
import com.socialite.common.state.DataState
import com.socialite.data.di.NonAuthorizationService
import com.socialite.data.network.SoliteServices
import com.socialite.data.schema.response.TokenResponse
import com.socialite.data.schema.helper.ResponseHandler.handleErrorMessage
import com.socialite.data.repository.AccountRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    @NonAuthorizationService private val service: SoliteServices,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : AccountRepository {
    override suspend fun login(email: String, password: String): Flow<DataState<String>> {
        return flow {
            val result = try {
                val response = handleErrorMessage {
                    service.login(email, password)
                }

                DataState.Success(response.getTokenOrError())

            } catch (e: Exception) {
                DataState.Error(e.toError<String>())
            }
            emit(result)
        }.onStart {
            emit(DataState.Loading)
        }.flowOn(dispatcher)
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): Flow<DataState<String>> {
        return flow {
            val result = try {
                val response = handleErrorMessage {
                    service.register(name, email, password, storeName)
                }

                DataState.Success(response.getTokenOrError())

            } catch (e: Exception) {
                DataState.Error(e.toError<String>())
            }
            emit(result)
        }.onStart {
            emit(DataState.Loading)
        }.flowOn(dispatcher)
    }

    private fun ApiResponse<TokenResponse>.getTokenOrError(): String {
        check(this.error.isNullOrEmpty()) {
            this.error ?: ""
        }

        val token = this.data?.token
        check(!token.isNullOrEmpty()) {
            "Token response null or empty"
        }

        return token
    }
}

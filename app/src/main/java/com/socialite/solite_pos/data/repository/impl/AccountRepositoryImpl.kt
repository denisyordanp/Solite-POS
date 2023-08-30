package com.socialite.solite_pos.data.repository.impl

import com.socialite.solite_pos.network.SoliteServices
import com.socialite.solite_pos.data.schema.response.TokenResponse
import com.socialite.solite_pos.data.schema.response.ApiResponse
import com.socialite.solite_pos.network.helper.ResponseHandler.handleErrorMessage
import com.socialite.solite_pos.data.repository.AccountRepository
import com.socialite.solite_pos.di.NonAuthorizationService
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    @NonAuthorizationService private val service: SoliteServices,
) : AccountRepository {
    override suspend fun login(email: String, password: String): String {
        val response = handleErrorMessage {
            service.login(email, password)
        }

        return response.getTokenOrError()
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): String {
        val response = handleErrorMessage {
            service.register(name, email, password, storeName)
        }
        return response.getTokenOrError()
    }

    private fun ApiResponse<TokenResponse>.getTokenOrError(): String {
        check(this.error.isNullOrEmpty())

        val token = this.data?.token
        check(!token.isNullOrEmpty()) {
            "Token response null or empty"
        }

        return token
    }
}

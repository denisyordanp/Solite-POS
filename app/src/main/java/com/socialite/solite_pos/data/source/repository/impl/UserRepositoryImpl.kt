package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.preference.UserPreferences
import com.socialite.solite_pos.data.source.remote.SoliteServices
import com.socialite.solite_pos.data.source.remote.response.helper.ResponseHandler.handleErrorMessage
import com.socialite.solite_pos.data.source.repository.UserRepository

class UserRepositoryImpl(
    private val service: SoliteServices,
    private val userPreference: UserPreferences
) : UserRepository {
    override suspend fun login(email: String, password: String): String {
        val response = handleErrorMessage {
            service.login(email, password)
        }

        val error = response.error
        if (error.isNullOrEmpty().not()) throw IllegalStateException(error)

        val token = response.data?.token
        if (token.isNullOrEmpty()) throw IllegalStateException("Token response null or empty")

        return token
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

        val error = response.error
        if (error.isNullOrEmpty().not()) throw IllegalStateException(error)

        val token = response.data?.token
        if (token.isNullOrEmpty()) throw IllegalStateException("Token response null or empty")

        return token
    }

    override fun insertToken(token: String) {
        userPreference.setUserToken(token)
    }

    override fun getToken(): String = userPreference.getUserToken()
}

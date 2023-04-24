package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.remote.SoliteServices
import com.socialite.solite_pos.data.source.repository.UserRepository

class UserRepositoryImpl(
    private val service: SoliteServices
) : UserRepository {
    override suspend fun login(email: String, password: String): String {
        val response = service.login(email, password)
        val token = response.data?.token
        if (token.isNullOrEmpty()) throw IllegalArgumentException("Token response null or empty")

        return token
    }
}
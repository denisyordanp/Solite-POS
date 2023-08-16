package com.socialite.solite_pos.data.repository

interface AccountRepository {
    suspend fun login(
        email: String,
        password: String
    ): String

    suspend fun register(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): String
}
